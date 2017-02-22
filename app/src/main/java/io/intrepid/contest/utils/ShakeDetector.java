package io.intrepid.contest.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

public class ShakeDetector {

    private static final int THRESHOLD = 13;
    private static final int SHAKES_COUNT = 5;
    private static final int SHAKES_PERIOD = 2;

    @NonNull
    public static Flowable<?> create(@NonNull Context context) {
        return createAccelerationObservable(context)
                .map(sensorEvent -> new XEvent(sensorEvent.timestamp, sensorEvent.values[0]))
                .filter(xEvent -> Math.abs(xEvent.x) > THRESHOLD)
                .buffer(2, 1)
                .filter(buf -> buf.get(0).x * buf.get(1).x < 0)
                .map(buf -> buf.get(1).timestamp / 1000000000f)
                .buffer(SHAKES_COUNT, 1)
                .filter(buf -> buf.get(SHAKES_COUNT - 1) - buf.get(0) < SHAKES_PERIOD)
                .throttleFirst(SHAKES_PERIOD, TimeUnit.SECONDS);
    }

    @NonNull
    private static Flowable<SensorEvent> createAccelerationObservable(@NonNull Context context) {
        SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION);
        if (sensorList == null || sensorList.isEmpty()) {
            throw new IllegalStateException("Device has no linear acceleration sensor");
        }
        Sensor sensor = sensorList.get(0);

        return Flowable.create(new FlowableOnSubscribe<SensorEvent>() {
            @Override public void subscribe(final FlowableEmitter<SensorEvent> emitter) throws Exception {
                final SensorEventListener listener = new SensorEventListener() {
                    @Override public void onSensorChanged(SensorEvent sensorEvent) {
                        emitter.onNext(sensorEvent);
                    }

                    @Override public void onAccuracyChanged(Sensor sensor1, int accuracy) {
                    }
                };

                emitter.setCancellable(() -> mSensorManager.unregisterListener(listener, sensor));

                mSensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME);
            }
        }, BackpressureStrategy.DROP);
    }

    private static class XEvent {
        final long timestamp;
        final float x;

        private XEvent(long timestamp, float x) {
            this.timestamp = timestamp;
            this.x = x;
        }
    }
}
