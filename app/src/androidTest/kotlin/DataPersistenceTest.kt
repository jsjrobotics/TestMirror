import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ServiceTestRule
import android.support.test.runner.AndroidJUnit4
import com.jsjrobotics.testmirror.service.http.BackendService
import com.jsjrobotics.testmirror.IBackend
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class DataPersistenceTest {
    @JvmField
    @Rule
    var serviceRule: ServiceTestRule = ServiceTestRule()

    @Test
    fun testServiceBinder() {
        val binder = serviceRule.bindService(
                Intent(InstrumentationRegistry.getTargetContext(), BackendService::class.java))
        val service = IBackend.Stub.asInterface(binder)
        assertNotNull("Returned binder should not be null", service)
    }
}