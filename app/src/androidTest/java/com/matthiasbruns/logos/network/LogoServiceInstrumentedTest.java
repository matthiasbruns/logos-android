package com.matthiasbruns.logos.network;

import com.matthiasbruns.logos.R;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import java.util.List;

import rx.functions.Action1;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by mbruns on 30/11/2016.
 */
@RunWith(AndroidJUnit4.class)
public class LogoServiceInstrumentedTest {

    static final String TAG = LogoServiceInstrumentedTest.class.getName();

    @Test
    public void getLogos() {
        final LogoService service = _createService();
        service.get().subscribe(new Action1<List<Logo>>() {
            @Override
            public void call(final List<Logo> logos) {
                assertTrue(logos.size() > 0);
            }
        });
    }

    @Test
    public void getLogosByQuery() {
        String[] queries = {
                "google",
                "1",
                "sound",
                "flattr"
        };

        final LogoService service = _createService();

        for (String query : queries) {
            service.query(query).subscribe(new Action1<List<Logo>>() {
                @Override
                public void call(final List<Logo> logos) {
                    assertTrue(logos.size() > 0);
                }
            });
        }
    }

    @Test
    public void getLogosByQueryAndSource() {
        final LogoService service = _createService();

        String[] queries = {
                "500px",
                "air",
                "appnet"
        };
        String source = "simpleicons";

        for (String query : queries) {
            service.query(query, source).subscribe(new Action1<List<Logo>>() {
                @Override
                public void call(final List<Logo> logos) {
                    assertTrue(logos.size() > 0);
                }
            });
        }

        queries = new String[]{
                "100tb",
                "android",
                "centos"
        };
        source = "gilbarbara";

        for (String query : queries) {
            service.query(query, source).subscribe(new Action1<List<Logo>>() {
                @Override
                public void call(final List<Logo> logos) {
                    assertTrue(logos.size() > 0);
                }
            });
        }
    }

    @Test
    public void pingService() {
        final Context appContext = InstrumentationRegistry.getTargetContext();
        final String endpoint = appContext.getResources().getString(R.string.service_logo_endpoint);
        assertNotNull(endpoint);

        final LogoService service = new LogoServiceFactory(appContext).create(endpoint);
        assertNotNull(service);

        service.get().subscribe(new Action1<List<Logo>>() {
            @Override
            public void call(final List<Logo> logos) {
                Log.i(TAG, logos.toString());
                assertNotNull(logos);
            }
        });
    }

    private LogoService _createService() {
        final Context appContext = InstrumentationRegistry.getTargetContext();
        final String endpoint = appContext.getResources().getString(R.string.service_logo_endpoint);
        return new LogoServiceFactory(appContext).create(endpoint);
    }
}
