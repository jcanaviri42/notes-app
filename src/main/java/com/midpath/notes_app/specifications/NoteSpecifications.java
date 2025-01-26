package com.midpath.notes_app.specifications;

import com.midpath.notes_app.model.Note;
import com.midpath.notes_app.model.Tag;
import com.midpath.notes_app.model.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class NoteSpecifications {
    public static Specification<Note> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<Note> hasContent(String content) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("content"), "%" + content + "%");
    }

    public static Specification<Note> hasTag(Long tagId) {
        return (root, query, criteriaBuilder) -> {
            Join<Note, Tag> tagJoin = root.join("tags");
            return criteriaBuilder.equal(tagJoin.get("id"), tagId);
        };
    }

    public static Specification<Note> hasTagName(String tagName) {
        return (root, query, criteriaBuilder) -> {
            Join<Note, Tag> tagJoin = root.join("tags");
            return criteriaBuilder.like(tagJoin.get("name"), "%" + tagName + "%");
        };
    }

    public static Specification<Note> byUser(User user) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user"), user);
    }

    public static Specification<Note> buildSpecification(String title, String content, List<Long> tagIds, List<String> tagNames, User user) {
        List<Specification<Note>> specifications = new ArrayList<>();

        specifications.add(byUser(user));

        if (title != null && !title.isEmpty()) {
            specifications.add(hasTitle(title));
        }
        if (content != null && !content.isEmpty()) {
            specifications.add(hasContent(content));
        }
        if (tagIds != null) {
            for (Long tagId : tagIds) {
                specifications.add(hasTag(tagId));
            }
        }
        if (tagNames != null) {
            for (String tagName : tagNames) {
                specifications.add(hasTagName(tagName));
            }
        }
        Specification<Note> result = specifications.getFirst();
        for (int i = 1; i < specifications.size(); i++) {
            result = result.and(specifications.get(i));
        }
        return result;
    }
}
