/*
 *  Copyright 2019 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package guru.sfg.brewery.beer_service.domain;

import guru.sfg.brewery.model.BeerStyleEnum;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Created by jt on 2019-01-26.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beer {

    @Id
    @GeneratedValue(generator = "UUID")
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @JdbcTypeCode(SqlTypes.CHAR)
    //@Type(type="org.hibernate.type.UUIDCharType")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp lastModifiedDate;

    public boolean isNew() {
        return this.id == null;
    }

    private String beerName;

    @Enumerated(EnumType.STRING)
    private BeerStyleEnum beerStyle;

    @Column(unique = true)
    private String upc;

    private BigDecimal price;
    /**
     * Min on hand qty - used to trigger brew
     */
    private Integer minOnHand;
    private Integer quantityToBrew;
    private Integer quantityOnHand;

}
