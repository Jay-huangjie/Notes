package com.jie.notes.util;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huangjie on 2017/6/15.
 * <p>
 * rxjava辅助类
 */

public class RxJavaUtil {

    /*
    * 运行在IO线程不需要处理返回结果
    * */
    public static void StarIOThreadTransaction(final ToSubscribe iview) {
        Observable.create(new ObservableOnSubscribe<Object>() {

            @Override
            public void subscribe(ObservableEmitter<Object> observableEmitter) throws Exception {
                iview.subscribe();
                observableEmitter.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe();
    }

    /*
    * 运行在IO线程处理返回结果
    * */
    public static void StarIOSubscribeTransaction(final ToSubscribe iview, final ToNext iView2) {
        Observable.create(new ObservableOnSubscribe<Object>() {

            @Override
            public void subscribe(ObservableEmitter<Object> observableEmitter) throws Exception {
                observableEmitter.onNext(iview.subscribe());
                observableEmitter.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<Object>() {

            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Object o) {
                iView2.onNext(o);
            }

            @Override
            public void onError(Throwable throwable) {
                iView2.onError(throwable.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }


    public interface ToSubscribe {
        Object subscribe();
    }

    public interface ToNext {
        void onNext(Object t);

        void onError(String errorMsg);
    }
}
