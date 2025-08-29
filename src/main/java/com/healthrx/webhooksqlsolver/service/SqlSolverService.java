package com.healthrx.webhooksqlsolver.service;

import org.springframework.stereotype.Service;

@Service
public class SqlSolverService {

    public String solveSqlProblem(String regNo) {
        return solveQuestion1();
    }
    
    private String solveQuestion1() {
        return """
            SELECT 
                p.AMOUNT as SALARY,
                CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) as NAME,
                TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) as AGE,
                d.DEPARTMENT_NAME
            FROM PAYMENTS p
            INNER JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID
            INNER JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
            WHERE DAY(p.PAYMENT_TIME) != 1
            ORDER BY p.AMOUNT DESC
            LIMIT 1;
            """;
    }
}
