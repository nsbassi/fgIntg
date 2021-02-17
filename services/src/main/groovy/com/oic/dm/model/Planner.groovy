package com.oic.dm.model

class Planner {
    String SR_INSTANCE_CODE, PLANNER_CODE, DESCRIPTION, EMPLOYEE_NUMBER, END = 'END'
    Date DISABLE_DATE

    String toString() {
        [SR_INSTANCE_CODE, PLANNER_CODE, DESCRIPTION, EMPLOYEE_NUMBER, DISABLE_DATE, END]
                .collect { it ? "\"$it\"" : '' }.join(',') + '\n'
    }
}
