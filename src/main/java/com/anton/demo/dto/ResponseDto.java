package com.anton.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author by nadeeshan_fdz
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T> implements Serializable {
    public String message;
    public int status;
    private Object data;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

}
