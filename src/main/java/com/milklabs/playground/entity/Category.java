package com.milklabs.playground.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;

/**
 * A Category.
 */
@Entity
@Table(name = "category", schema = "playground_spring")
@Getter
@Setter
@Transactional
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Integer categoryId;

	@Column(name = "category_name", nullable = false)
	private String categoryName;

	@ManyToOne(fetch = FetchType.EAGER) 
	@JoinColumn(name = "parent_category_id")
	private Category categoryParent;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder().append("categoryId: [").append(categoryId).append("] categoryName: [")
				.append(categoryName).append("] categoryParent id [").append(categoryParent).append("]");
		return sb.toString();
	}
}
