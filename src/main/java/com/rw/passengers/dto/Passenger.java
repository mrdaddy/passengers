package com.rw.passengers.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@ApiModel(description = "Данные о пассажире")
public class Passenger {
    public enum SEX_TYPE {M,F}
    @ApiModelProperty(example = "1", required = false, value = "Уникальный идентификатор пассажира из СППД. Если пассажир вводится первый раз - заполнять не нужно", dataType = "long")
    private long id;

    @ApiModelProperty(example = "Иван", required = true, value = "Имя пассажира (при покупке по глобальной цене допускается только латиница)", dataType = "String")
    @Size(min = 1, max = 32)
    private String firstName;

    @ApiModelProperty(example = "Иванович", required = false, value = "Отчество пассажира (при покупке по глобальной цене не заполняется)", dataType = "String")
    @Size(max = 32)
    private String patronymic;

    @ApiModelProperty(example = "Иванов", required = true, value = "Фамилия пассажира (при покупке по глобальной цене допускается только латиница)", dataType = "String")
    @Size(min = 1, max = 32)
    private String lastName;

    @ApiModelProperty(example = "1", required = false, value = "Количество мест для данного пассажира", dataType = "String")
    @Max(4)
    private int placesCount;

    @ApiModelProperty(example = "M", required = false, value = "Пол пассажира (необходим для продажи по глобальным ценам и вагонам с признаком М/Ж)", dataType = "String")
    private SEX_TYPE sex;

    @ApiModelProperty(example = "1990-01-30", required = false, value = "Дата рождения пассажира (необходим для продажи по глобальным ценам, для детских билетов и билетов без мест)", dataType = "Date")
    @Past
    private Date birthday;

    @ApiModelProperty(example = "BLR", required = false, value = "Код государства выдачи документа пассажира из справочника “Справочник государств выдачи документа, удостоверяющего личность пассажира”, необходим для продажи по глобальным ценам", dataType = "String")
    @Size(max = 3)
    private String country = "BLR";

    @ApiModelProperty(example = "ПБ", required = true, value = "Код типа документа из справочника “Справочник типов документов, удостоверяющих личность пассажира”", dataType = "String")
    @Size(min = 2, max = 2)
    private String documentType ;

    @ApiModelProperty(example = "КН2014222", required = true, value = "Номер документа, удостоверяющего личность пассажира", dataType = "String")
    @Size(min = 1, max = 16)
    private String documentNumber;
}
