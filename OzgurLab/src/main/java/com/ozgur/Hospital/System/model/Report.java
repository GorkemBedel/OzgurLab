package com.ozgur.Hospital.System.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.descriptor.java.PrimitiveByteArrayJavaType;
import org.springframework.data.domain.Persistable;

import java.sql.Types;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "reports")
public class Report implements Persistable<Long>{

    @Id
    @Column(name = "report_id")
    private Long report_id;
    private String patientName;
    private String sickness;
    private String sicknessDetails;
    private Long patientTC;
    private Long laborantId;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "reportDate")
    private LocalDate reportDate;

    @Lob
    @JsonIgnore
    @JdbcTypeCode(Types.BINARY)
    private byte[] reportPhoto;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "patient", nullable = false)
    private Patient patient;

    @Transient
    private boolean isUpdated = false;

    @JsonIgnore
    //Many reports are belong to one laborant
    @ManyToOne
    @JoinColumn(name = "laborant", nullable = false)
    private Laborant laborant;

    @Override
    public Long getId() {
        return report_id;
    }

    @Override
    public boolean isNew() {
        return !this.isUpdated || report_id == null;
    }




}
