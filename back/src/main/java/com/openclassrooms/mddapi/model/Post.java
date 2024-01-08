package com.openclassrooms.mddapi.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="post_id")
	private Long id;

	@Column(nullable = false)
	@NotNull
	@Size(max = 80)
	private String title;

	@Column(nullable = false)
	@NotNull
	@Size(max = 20000)
	private String content;

	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@ManyToOne
	@JoinColumn(name = "topic_id", nullable = false)
	private Topic topic;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User postAuthor;

	@OneToMany(mappedBy = "post")
	private List<Comment> comments = new ArrayList<>();
}
