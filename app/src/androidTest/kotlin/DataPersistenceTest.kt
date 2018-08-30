import android.support.test.rule.ServiceTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.runner.RunWith
import android.support.test.InstrumentationRegistry
import android.content.Intent
import android.os.IBinder
import com.jsjrobotics.testmirror.DataPersistenceService
import com.jsjrobotics.testmirror.IDataPersistence
import org.junit.Assert.assertNotNull
import org.junit.Test


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