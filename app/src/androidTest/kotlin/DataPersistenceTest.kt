import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ServiceTestRule
import android.support.test.runner.AndroidJUnit4
import com.jsjrobotics.testmirror.DataPersistenceService
import com.jsjrobotics.testmirror.IDataPersistence
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
                Intent(InstrumentationRegistry.getTargetContext(), DataPersistenceService::class.java))
        val service = IDataPersistence.Stub.asInterface(binder)
        assertNotNull("Returned binder should not be null", service)
    }
}