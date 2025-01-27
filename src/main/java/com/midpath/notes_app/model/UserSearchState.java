package com.midpath.notes_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "user_search_states", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "title", "content", "tag_ids", "tag_names"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String titleFilter;

    private String contentFilter;

    @ElementCollection
    @CollectionTable(name = "user_search_state_tag_ids", joinColumns = @JoinColumn(name = "user_search_state_id"))
    @Column(name = "tag_ids_filter")
    private List<Long> tagIdsFilter;

    @ElementCollection
    @CollectionTable(name = "user_search_state_tag_names", joinColumns = @JoinColumn(name = "user_search_state_id"))
    @Column(name = "tag_names")
    private List<String> tagNames;
}
