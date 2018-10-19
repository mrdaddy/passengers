package com.rw.passengers.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Информация об документе. В поле id - код документа." + "1 - ПБ\n" + "2-ПН\n" + "3-ЗП\n" + "5-ПМ\n" + "6-СР\n" + "7-ВБ\n" + "8-УЛ\n" + "9-СУ\n" + "10-СО\n" + "11-ВЖ\n" + "12-ВУ\n")
public class Document {
    @ApiModelProperty(example = "1" , required = true, value = "ПБ - Паспорт Республики Беларусь", dataType = "Integer")
    private Integer id;

    @ApiModelProperty(example = "КН2014222", required = true, value = "Код документа", dataType = "String")
    private String number;
}