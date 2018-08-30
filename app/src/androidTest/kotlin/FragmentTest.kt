import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v4.app.Fragment
import com.jsjrobotics.testmirror.EspressoTestActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class FragmentTest<T: Fragment> {
    @JvmField
    @Rule
    var mActivityRule: ActivityTestRule<EspressoTestActivity> = ActivityTestRule(EspressoTestActivity::class.java, true, false)


    protected val TEST_TAG: String = "test_fragment"

    @Before
    fun setup() {
        val testSubject = instantiateFragment()
        mActivityRule.launchActivity(null);
        addFragmentToActivity(testSubject)
    }

    abstract fun instantiateFragment(): T

    @After
    fun tearDown() {
        mActivityRule.finishActivity()
    }

    private fun addFragmentToActivity(testSubject: Fragment) {
        mActivityRule.activity
                .supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, testSubject, TEST_TAG)
                .commit()
    }

}