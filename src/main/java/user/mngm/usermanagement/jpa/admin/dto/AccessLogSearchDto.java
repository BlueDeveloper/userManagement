package user.mngm.usermanagement.jpa.admin.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.StringPath;
import lombok.Data;

import java.sql.Date;

@Data
public class AccessLogSearchDto {
    private String rHost;
    private String rUri;
    private String accDat;
    private String accDatStart;
    private String accDatEnd;


    @QueryProjection
    public AccessLogSearchDto(String rHost, String rUri, String accDat) {
        this.rHost = rHost;
        this.rUri = rUri;
        this.accDat = accDat;
    }
}
