package com.bideafactory.house_reservation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "book")
public class Book {

    @Id
    private String id;

    private String name;
    private String lastname;
    private int age;
    private String phoneNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private String houseId;
    private String discountCode;

}