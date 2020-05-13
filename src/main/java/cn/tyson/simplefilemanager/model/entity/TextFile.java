package cn.tyson.simplefilemanager.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "text_file")
public class TextFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    private Integer id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "text")
    private String text;

    @Column(name = "path")
    private String path;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}
