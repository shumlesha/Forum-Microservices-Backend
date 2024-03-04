package com.example.forum.repository.impl;

import com.example.forum.dto.Message.MessageFilter;
import com.example.forum.models.Message;
import com.example.forum.repository.FilterMessageRepository;
import com.example.forum.service.CategoryService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.example.forum.models.QMessage.message;


@RequiredArgsConstructor
public class FilterMessageRepositoryImpl implements FilterMessageRepository {

    private final EntityManager entityManager;
    private final CategoryService categoryService;

    @Override
    public List<Message> findByFilter(MessageFilter filter) {

        BooleanBuilder builder = new BooleanBuilder();

        Optional.ofNullable(filter.content()).ifPresent(content -> builder.and(message.content.containsIgnoreCase(content)));
        Optional.ofNullable(filter.startDate()).ifPresent(startDate -> builder.and(message.createTime.after(startDate)));
        Optional.ofNullable(filter.endDate()).ifPresent(endDate -> builder.and(message.createTime.before(endDate)));
        Optional.ofNullable(filter.topicId()).ifPresent(topicId -> builder.and(message.topic.id.eq(topicId)));

        if (filter.categoryId() != null) {
            Set<UUID> subCategories = categoryService.getSubcategoryIds(filter.categoryId());
            subCategories.add(filter.categoryId());
            builder.and(message.topic.category.id.in(subCategories));
        }

        return new JPAQuery<>(entityManager).select(message).from(message)
                .where(builder).fetch();

    }
}
