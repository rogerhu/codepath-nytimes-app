package codepath.com.nytimesfun;

import android.support.annotation.IntDef;

public class CheckboxOptions {

    @IntDef(flag=true,
            value = {ARTS, FASHION, SPORTS} )
    public @interface CHOICES {}

    public static final int ARTS = 1;
    public static final int FASHION = 2;
    public static final int SPORTS = 3;


}
