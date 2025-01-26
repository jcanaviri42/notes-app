package com.midpath.notes_app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "user_search_states")
@Getter
@Setter
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

    private List<Long> tagIdsFilter;

    private List<String> tagNames;
}
