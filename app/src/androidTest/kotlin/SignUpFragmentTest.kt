import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import android.widget.EditText
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.signup.SignUpFragment
import org.hamcrest.CoreMatchers.allOf
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpFragmentTest : FragmentTest<SignUpFragment>() {
    lateinit var testSubject: SignUpFragment

    override fun instantiateFragment(): SignUpFragment {
        testSubject = SignUpFragment()
        return testSubject
    }


    @Test
    fun testEmailInputFound() {
        val emailEditTextMatcher = withHint(R.string.enter_email)
        val assertions = matches(allOf(
                isDisplayed(),
                isAssignableFrom(EditText::class.java)
        ))
        onView(emailEditTextMatcher).check(assertions)
    }

    @Test
    fun testPasswordInputFound() {
        val passwordEditTextMatcher = withHint(R.string.enter_password)
        val assertions = matches(allOf(
                isDisplayed(),
                isAssignableFrom(EditText::class.java)
        ))
        onView(passwordEditTextMatcher).check(assertions)
    }

    @Test
    fun testUserNameInputFound() {
        val usernameEditTextMatcher = withHint(R.string.enter_full_name)
        val assertions = matches(allOf(
                isDisplayed(),
                isAssignableFrom(EditText::class.java)
        ))
        onView(usernameEditTextMatcher).check(assertions)
    }
}