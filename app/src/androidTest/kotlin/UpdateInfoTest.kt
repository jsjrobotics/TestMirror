import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.widget.DatePicker
import android.widget.EditText
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.updateInfo.UpdateInfoFragment
import org.hamcrest.CoreMatchers
import org.junit.Test

class UpdateInfoTest : FragmentTest<UpdateInfoFragment>() {
    private lateinit var testSubject: UpdateInfoFragment

    override fun instantiateFragment(): UpdateInfoFragment {
        testSubject = UpdateInfoFragment()
        return testSubject
    }

    @Test
    fun testBirthdayInputFound() {
        val birthdayMatcher = ViewMatchers.isAssignableFrom(DatePicker::class.java)
        val assertions = ViewAssertions.matches(CoreMatchers.allOf(
                ViewMatchers.isDisplayed()
        ))
        Espresso.onView(birthdayMatcher).check(assertions)
    }

    @Test
    fun testLocationInputFound() {
        val locationEditTextMatcher = ViewMatchers.withHint(R.string.enter_location)
        val assertions = ViewAssertions.matches(CoreMatchers.allOf(
                ViewMatchers.isDisplayed(),
                ViewMatchers.isAssignableFrom(EditText::class.java)
        ))
        Espresso.onView(locationEditTextMatcher).check(assertions)
    }
}