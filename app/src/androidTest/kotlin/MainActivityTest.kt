import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isClickable
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v4.app.Fragment
import com.jsjrobotics.testmirror.activities.MainActivity
import com.jsjrobotics.testmirror.R
import com.jsjrobotics.testmirror.signup.SignUpFragment
import com.jsjrobotics.testmirror.welcome.WelcomeFragment
import org.hamcrest.CoreMatchers.allOf
import org.junit.*
import org.junit.runner.RunWith


// Unable to get mockito working here, hence verifying actions by checking displayed fragments
// Note tests sometimes fails when running all tests but not individually : https://issuetracker.google.com/issues/36932872
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @JvmField
    @Rule
    var testRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java, true, false)

    @Before
    fun setup() {
        testRule.launchActivity(null);
    }

    @After
    fun tearDown() {
        testRule.finishActivity()
    }


    @Test
    fun testLightDisplayFragmentExists() {
        val fragmentFound : Fragment? = getFragmentByTag(WelcomeFragment.TAG)
        Assert.assertNotNull("Welcome fragment should be present at startup", fragmentFound)
    }

    @Test
    fun testSignUpButton(){
        val signupMatcher = ViewMatchers.withText(R.string.signup)
        val assertions = matches(allOf(isDisplayed(), isClickable()))
        onView(signupMatcher).check(assertions)
        onView(signupMatcher).perform(click())
        val fragmentFound : Fragment? = getFragmentByTag(SignUpFragment.TAG)
        Assert.assertNotNull("Signup fragment should be present after click", fragmentFound)
    }

    @Test
    fun testLoginButtonExists(){
        val loginMatcher = ViewMatchers.withText(R.string.login)
        val assertions = matches(allOf(isDisplayed(), isClickable()))
        Espresso.onView(loginMatcher).check(assertions)
    }


    private fun getFragmentByTag(tag: String): Fragment? {
        return testRule.activity
                .supportFragmentManager
                .findFragmentByTag(tag)
    }
}