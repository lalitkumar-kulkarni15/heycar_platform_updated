package com.heycar.platform.testsuite;

import com.heycar.platform.controller.ListingControllerIntJsonTest;
import com.heycar.platform.controller.ListingControllerIntCsvTest;
import com.heycar.platform.controller.ListingControllerJsonUnitTest;
import com.heycar.platform.service.ListingSvcImplUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ListingControllerIntJsonTest.class,
        ListingControllerIntCsvTest.class,
        ListingControllerJsonUnitTest.class,
        ListingSvcImplUnitTest.class
})
public class ListingTestSuite {
}
