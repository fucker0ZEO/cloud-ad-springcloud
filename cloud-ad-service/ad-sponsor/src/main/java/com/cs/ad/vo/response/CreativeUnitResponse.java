package com.cs.ad.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fucker
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreativeUnitResponse {

    private List<Long> ids;
}
