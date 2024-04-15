package com.mindconnect.mindconnect.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor


@NamedNativeQuery(
        name = "find_most_recent_messages_with_all_users",
        query = "SELECT cm.id, cm.created_at, cm.updated_at, cm.message, cm.receiver_id, cm.sender_id " +
                "FROM chat_messages cm " +
                "         JOIN LATERAL ( " +
                "    SELECT CASE " +
                "               WHEN cm1.sender_id = :currentUser THEN cm1.receiver_id " +
                "               ELSE cm1.sender_id " +
                "               END AS other_user_id, " +
                "           MAX(cm1.created_at) AS max_created_at " +
                "    FROM chat_messages as cm1 " +
                "    WHERE cm1.sender_id = :currentUser OR cm1.receiver_id = :currentUser " +
                "    GROUP BY other_user_id " +
                ") AS recent_messages ON ( " +
                "                            (cm.sender_id = :currentUser AND cm.receiver_id = recent_messages.other_user_id) " +
                "                                OR " +
                "                            (cm.sender_id = recent_messages.other_user_id AND cm.receiver_id = :currentUser) " +
                "                            ) AND cm.created_at = recent_messages.max_created_at " +
                "ORDER BY recent_messages.max_created_at DESC " +
                "LIMIT :limit OFFSET :offset",
        resultSetMapping = "chat"
)

@SqlResultSetMapping(
        name = "chat",
        classes = @ConstructorResult(
                targetClass = RecentChatDto.class,
                columns = {
                        @ColumnResult(name = "id", type = UUID.class),
                        @ColumnResult(name = "created_at", type = Timestamp.class),
                        @ColumnResult(name = "updated_at", type = Timestamp.class),
                        @ColumnResult(name = "message", type = String.class),
                        @ColumnResult(name = "receiver_id", type = UUID.class),
                        @ColumnResult(name = "sender_id", type = UUID.class)
                }
        )
)
public class RecentChatDto {
    @Id
    private UUID id;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    private String message;
    @Column(name = "receiver_id")
    private UUID receiverId;
    @Column(name = "sender_id")
    private UUID senderId;
    public LocalDateTime getCreatedAt() {
        return createdAt.toInstant()
                .atZone(ZoneId.of("UTC")).toLocalDateTime();
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt.toInstant()
                .atZone(ZoneId.of("UTC")).toLocalDateTime();
    }
}
