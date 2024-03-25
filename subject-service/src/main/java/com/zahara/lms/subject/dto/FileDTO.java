package com.zahara.lms.subject.dto;

import com.zahara.lms.shared.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FileDTO extends BaseDTO<Long> {
    @NotBlank(message = "File name is mandatory")
    private String fileName;

    @NotBlank(message = "Content type is mandatory")
    private String contentType;

    @NotNull(message = "Data is mandatory")
    private byte[] data;

    @NotNull(message = "Upload time is mandatory")
    private LocalDateTime uploadTimestamp;

    @NotNull(message = "Bundle is mandatory")
    private BundleDTO bundle;


}