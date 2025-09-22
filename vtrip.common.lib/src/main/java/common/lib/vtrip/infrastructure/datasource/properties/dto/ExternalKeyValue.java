package common.lib.vtrip.infrastructure.datasource.properties.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExternalKeyValue {
    private String key;
    private String value;
}
