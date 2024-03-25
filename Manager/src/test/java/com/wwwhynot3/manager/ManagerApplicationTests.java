package com.wwwhynot3.manager;

import com.wwwhynot3.manager.Config.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
class ManagerApplicationTests {
    private final GlobalExceptionHandler globalExceptionHandler;

}
