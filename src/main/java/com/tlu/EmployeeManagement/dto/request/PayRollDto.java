package com.tlu.EmployeeManagement.dto.request;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;



@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PayRollDto {
    Integer empId;
    
    BigDecimal allowance; // tro cap

    BigDecimal bonus; // thuong 

    BigDecimal deduction; // tru

}
