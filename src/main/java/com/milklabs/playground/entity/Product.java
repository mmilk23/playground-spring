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
 * A Product.
 */
@Entity
@Table(name = "product", schema = "playground_spring")
@Getter
@Setter
@Transactional
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Integer productId;

	@Column(name = "product_name", nullable = false)
	private String productName;

	@Column(name = "description")
	private String productDescription;

	@Column(name = "product_price", nullable = false)
	private Double productPrice;

	@Column(name = "product_available")
	private Boolean productAvailable;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id", nullable = false)
	private Category productCategory;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder().append("[Product] productId: [").append(productId).append("] name: [")
				.append(productName).append("] description id [").append(productDescription).append("]  price")
				.append(productPrice).append("] available [").append(productAvailable).append("] Category [")
				.append(productCategory).append("]");
		return sb.toString();
	}

}
