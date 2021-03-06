package mops.infrastructure.adapters.webflow.datepoll.webflowdtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import mops.infrastructure.adapters.webflow.dtos.GeneralDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class UploadDto extends GeneralDto implements Serializable {

    public static final long serialVersionUID = 9832573348L;

    private transient MultipartFile pollFile;
}
