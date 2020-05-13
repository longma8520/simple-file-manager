package cn.tyson.simplefilemanager.model.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class TextFileVo {
    @NotEmpty
    private String fileName;
    @NotEmpty
    private String text;
}
