package macaca.java.biz;

import java.io.File;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import macaca.client.MacacaClient;
import macaca.client.commands.Element;
import macaca.client.common.GetElementWay;
import macaca.java.biz.*;
import macaca.java.biz.BaseUtils;
import macaca.java.biz.ResultGenerator;

/**
 * 自定义的MacacaClient基类，用于在MacacaClient基础上封装自己的处理
 *
 * @author xiulian.yin
 */
public class BaseMacacaClient extends MacacaClient {

    // 定义平台类型枚举
    public enum PlatformType {
        IOS,
        ANDROID
    }

    // 定义手势
    public enum GesturePinchType {
        PINCH_IN, //两只手指向内缩小元素
        PINCH_OUT, // 两只手指向外放大元素
    }

    /**
     * 当前运行的平台，目前支持iOS Android
     */
    public PlatformType curPlatform;

    // 用于diff两张图片是否相同
    public static final String BEFORE_IMAGE_NAME = "before.png";
    public static final String AFTER_IMAGE_NAME = "after.png";

    public PlatformType getCurPlatform() {
        return curPlatform;
    }

    public void setCurPlatform(PlatformType curPlatform) {
        this.curPlatform = curPlatform;
    }


    /**
     * 查找控件，当同样的控件有多个时，返回第一个
     *
     * @param bean 要查找的UI控件对象
     * @throws Exception 查找中的异常
     */
    public Element findElement(macaca.java.biz.CommonUIBean bean) throws Exception {

        return findElementByIndex(bean, bean.index);

    }

    /**
     * 根据索引获取控件，当同样的控件可能存在多个时，查询返回的是一个数组，此时通过传入目标控件的索引获取指定控件
     *
     * @param bean  要查找的控件对象
     * @param index 目标控件index
     * @throws Exception 抛出异常
     */
    public Element findElementByIndex(macaca.java.biz.CommonUIBean bean, int index) throws Exception {
        if (curPlatform == PlatformType.IOS) {
            // 当前为iOS平台
            return getElement(bean.getIosBy(), bean.getIosValue(), index);
        } else {
            // 当前为安卓平台
            return getElement(bean.getAndroidBy(), bean.getAndroidValue(), index);
        }

    }

    /**
     * 当一类控件存在多个时，返回共有多少个该控件
     *
     * @param bean 要查找的控件
     * @return 控件数组个数
     * @throws Exception 抛出异常
     */
    public int countOfElment(macaca.java.biz.CommonUIBean bean) throws Exception {

        if (curPlatform == PlatformType.IOS) {
            // 当前为iOS平台
            return countOfElements(bean.getIosBy(), bean.getIosValue());
        } else {
            // 当前为安卓平台
            return countOfElements(bean.getAndroidBy(), bean.getAndroidValue());
        }
    }

    /**
     * 循环查找某个element，直到查找完毕
     *
     * @param bean 要查找的控件
     * @return 如果控件存在，返回控件对象，如果不存在，返回null
     * @throws Exception 抛出异常
     */
    public Element waitForElement(macaca.java.biz.CommonUIBean bean) throws Exception {

        if (curPlatform == PlatformType.IOS) {
            return waitForElement(bean.getIosBy(), bean.getIosValue(), bean.index);
        } else {
            return waitForElement(bean.getAndroidBy(), bean.getAndroidValue(), bean.index);
        }
    }

    /**
     * 循环查找某个element,直到查找完毕，适用于当前控件存在多个时，按照索引查找
     *
     * @param bean  要查找的控件
     * @param index 目标控件index
     * @throws Exception 抛出异常
     */
    public Element waitForElement(macaca.java.biz.CommonUIBean bean, int index) throws Exception {
        if (curPlatform == PlatformType.IOS) {
            return waitForElement(bean.getIosBy(), bean.getIosValue(), index);
        } else {
            return waitForElement(bean.getAndroidBy(), bean.getAndroidValue(), index);
        }
    }

    /**
     * 判断某个控件是否存在
     *
     * @param bean 要判断的UI控件对象
     * @return
     */
    public boolean isElementExist(macaca.java.biz.CommonUIBean bean) {
        try {

            if (curPlatform == PlatformType.IOS) {
                return isElementExist(bean.getIosBy(), bean.getIosValue(), bean.index);
            } else {
                return isElementExist(bean.getAndroidBy(), bean.getAndroidValue(), bean.index);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
//			ResultGenerator.catchedException(e);
            return false;
        }

    }

    /**
     * 判断某个控件是否存在
     *
     * @param bean  要判断的UI控件对象
     * @param index 要判断的控件的索引，当同样标记的控件存在多个时，用于唯一标记一个控件
     * @return
     */
    public boolean isElementExist(macaca.java.biz.CommonUIBean bean, int index) {
        try {

            if (curPlatform == PlatformType.IOS) {
                return isElementExist(bean.getIosBy(), bean.getIosValue(), index);
            } else {
                return isElementExist(bean.getAndroidBy(), bean.getAndroidValue(), index);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            macaca.java.biz.ResultGenerator.catchedException(e);
            return false;
        }

    }

    /**
     * 是否存在目标控件，如果当前没有该控件，在给定时间内循环查询，查询间隔以及时长通过setWaitElementTimeInterval,setWaitElementTimeout可设置
     *
     * @param bean 要查找的UI控件对象
     * @return true:控件存在 false:控件不存在
     */
    public boolean isElementExistAfterWaiting(macaca.java.biz.CommonUIBean bean) {
        try {
            if (this.isElementExist(bean)) {
                return true;
            } else {
                waitForElement(bean);
                return this.isElementExist(bean);
            }
        } catch (Exception e) {
            // TODO: handle exception
            macaca.java.biz.ResultGenerator.catchedException(e);
            return false;
        }

    }


    /**
     * 是否存在目标控件，如果当前没有该控件，在给定时间内循环查询，查询间隔以及时长通过setWaitElementTimeInterval,setWaitElementTimeout可设置
     *
     * @param bean  要判断的UI控件对象
     * @param index 要判断的控件的索引，当同样标记的控件存在多个时，用于唯一标记一个控件
     * @return true:控件存在 false:控件不存在
     */
    public boolean isElementExistAfterWaiting(macaca.java.biz.CommonUIBean bean, int index) {
        try {
            if (this.isElementExist(bean, index)) {
                return true;
            } else {
                waitForElement(bean, index);
                return this.isElementExist(bean, index);
            }
        } catch (Exception e) {
            // TODO: handle exception
            macaca.java.biz.ResultGenerator.catchedException(e);
            return false;
        }

    }

    /**
     * check if an parent element has specific child element
     *
     * @param parentBean 父控件
     * @param childBean  子控件
     * @return true:父控件含有子控件 false:父控件不含有子控件
     * @throws Exception 抛出异常
     */
    @Deprecated
    public boolean hasChildElement(macaca.java.biz.CommonUIBean parentBean, macaca.java.biz.CommonUIBean childBean) throws Exception {
        Element parentElement = findElement(parentBean);
        if (curPlatform == PlatformType.IOS) {
            return parentElement.hasChildElement(childBean.getIosBy(), childBean.getIosValue());
        } else {
            return parentElement.hasChildElement(childBean.getAndroidBy(), childBean.getAndroidValue());
        }
    }

    /**
     * find child elements for parent element
     *
     * @param parentBean 父控件
     * @param childBean  子控件
     * @return Json数组
     * @throws Exception 抛出异常
     */
    @Deprecated
    public JSONArray findChildElements(macaca.java.biz.CommonUIBean parentBean, macaca.java.biz.CommonUIBean childBean) throws Exception {
        Element parentElement = findElement(parentBean);
        if (curPlatform == PlatformType.IOS) {
            return parentElement.findChildElements(childBean.getIosBy(), childBean.getIosValue());
        } else {
            return parentElement.findChildElements(childBean.getAndroidBy(), childBean.getAndroidValue());
        }
    }


    /**
     * get count of Child element for given parent element
     *
     * @param parentBean 父控件
     * @param childBean  子控件
     * @return 数量
     * @throws Exception 抛出异常
     */
    @Deprecated
    public int countOfChildElements(macaca.java.biz.CommonUIBean parentBean, macaca.java.biz.CommonUIBean childBean) throws Exception {
        return findChildElements(parentBean, childBean).size();
    }

    /**
     * 自定义返回事件，对于iOS,通过右滑手势实现，对于Android，作用相当于点击系统返回键
     *
     * @throws Exception 抛出异常
     */
    public void customBack() throws Exception {
        macaca.java.biz.ResultGenerator.success("返回上一页", "右滑返回");
        if (curPlatform == PlatformType.IOS) {
            // iOS返回，通过模拟右滑返回实现
            try {
                drag(0, 100, 300, 100, 1);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                macaca.java.biz.ResultGenerator.catchedException(e);
            }
        } else {
            super.back();
        }
    }

    /**
     * 点击某一个控件，执行操作为 查找控件&点击控件 使用此API时不用先查找控件是否存在，如果控件存在，会执行点击事件，如果不存在，点击没有效果
     *
     * @param bean 要点击的控件
     */
    public void onclickBean(macaca.java.biz.CommonUIBean bean) {
        try {
            if (isElementExistAfterWaiting(bean)) {
                Element element = findElement(bean);
                element.click();
                macaca.java.biz.ResultGenerator.success("点击:" + bean.elementDesc, "");
            } else {
                macaca.java.biz.ResultGenerator.findElementFail(bean);
            }

        } catch (Exception e) {
            // TODO: handle exception
            macaca.java.biz.ResultGenerator.catchedException(e);
        }
    }

    /**
     * 点击某一个控件，执行操作为 查找控件-点击控件
     *
     * @param bean  目标控件
     * @param index 目标控件索引，当要查询的控件存在多个时用于唯一标识其中一个控件
     */
    public void onclickBeanAtIndex(macaca.java.biz.CommonUIBean bean, int index) {
        try {
            if (isElementExistAfterWaiting(bean, index)) {
                Element element = findElementByIndex(bean, index);
                element.click();
                macaca.java.biz.ResultGenerator.success("点击:" + bean.elementDesc + "[" + index + "]", "");
            } else {
                macaca.java.biz.ResultGenerator.findElementFail(bean);
            }

        } catch (Exception e) {
            // TODO: handle exception
            macaca.java.biz.ResultGenerator.catchedException(e);
        }
    }

    /**
     * 实现简单的输入
     *
     * @param bean  要输入文本的控件(一般是输入框)
     * @param input 要输入的内容
     */
    public void inputBean(macaca.java.biz.CommonUIBean bean, String input) {
        try {
            if (isElementExistAfterWaiting(bean)) {
                Element element = findElement(bean);
                element.sendKeys(input);
                macaca.java.biz.ResultGenerator.success("输入: " + bean.elementDesc + ";value:" + input, "");
            } else {
                macaca.java.biz.ResultGenerator.findElementFail(bean);
            }

        } catch (Exception e) {
            // TODO: handle exception
            macaca.java.biz.ResultGenerator.catchedException(e);
        }
    }

    /**
     * 清除指定控件文案
     *
     * @param bean 要清除文案的控件
     */
    public void clearText(macaca.java.biz.CommonUIBean bean)  {
        try {
            Element element = findElement(bean);
            element.clearText();
        } catch (Exception e) {
            // TODO: handle exception
            macaca.java.biz.ResultGenerator.catchedException(e);
        }

    }

    /**
     * 获取控件文案
     *
     * @param bean 目标控件
     * @return
     */
    public String getText(macaca.java.biz.CommonUIBean bean) throws Exception {
        try {
            Element element = findElement(bean);
            return element.getText();
        } catch (Exception e) {
            // TODO: handle exception
            macaca.java.biz.ResultGenerator.catchedException(e);
        }
        return null;
    }

    /**
     * 获取控件属性
     *
     * @param bean 目标控件
     * @param name 要获取的属性名字
     * Support: Android iOS Web(WebView).
     * iOS: 'isVisible', 'isAccessible', 'isEnabled', 'type', 'label', 'name', 'value',
     * Android: 'selected', 'description', 'text'
     * @return
     */
    public Object getProperty(macaca.java.biz.CommonUIBean bean, String name) throws Exception {
        try {
            Element element = findElement(bean);
            return element.getProperty(name);
        } catch (Exception e) {
            // TODO: handle exception
            macaca.java.biz.ResultGenerator.catchedException(e);
            return null;
        }
    }

    /**
     * 获取控件rect
     *
     * @param bean 目标控件
     *             Support:
     *             只支持Native控件，H5元素不支持
     * @return jsonObject eg: {x:100,y:100,width：100,height:100}
     */
    public Object getRect(macaca.java.biz.CommonUIBean bean) throws Exception {
        try {
            Element element = findElement(bean);
            return element.getRect();
        } catch (Exception e) {
            // TODO: handle exception
            macaca.java.biz.ResultGenerator.catchedException(e);
            return null;
        }
    }


    /**
     * 滑动当前页面到指定控件
     * Support: Android iOS Web(WebView)
     *
     * @param wayToFind 目标控件查找方式
     * @param value     目标控件查找值
     * @return true:找到控件，并完成滑动
     * false:控件不存在，滑到底部依然没有查到
     */
    public boolean scrollToElement(GetElementWay wayToFind, String value) throws Exception {

        return scrollToElementCustom(wayToFind, value, 100);

    }

    /**
     * 滑动当前页面到指定控件
     * Support: Android iOS Web(WebView)
     * @param wayToFind 目标控件查找方式
     * @param value     目标控件查找值
     * @param stepSize  滑动的跨度（幅度、距离）
     * @return true:找到控件，并完成滑动
     * false:控件不存在，滑到底部依然没有查到
     */
    public boolean scrollToElementCustom(GetElementWay wayToFind, String value, int stepSize) throws Exception {

        JSONObject windowSize;
        try {
            windowSize = getWindowSize();
            int windowWidth = windowSize.getIntValue("width");
            int windowHeight = windowSize.getIntValue("height");

            int startX = 0, endX = 0, startY = 0, endY = 0;
            startX = windowWidth - 20;
            endX = startX;
            startY = windowHeight * 3 / 5;
            endY = startY - stepSize;

            String beforeScreenShot = null, afterScreenShot = null;
            String beforePng = "before.png", afterPng = "after.png";

            while (!isElementExist(wayToFind, value)) {

                File shotOne = new File(beforePng);
                File shotTwo = new File(afterPng);
                beforeScreenShot = macaca.java.biz.BaseUtils.getFileMD5(shotOne);
                afterScreenShot = macaca.java.biz.BaseUtils.getFileMD5(shotTwo);
                if (beforeScreenShot != null
                        && beforeScreenShot.length() > 0) {
                    if (beforeScreenShot.equals(afterScreenShot)) {
                        // the same screen image ,it means current view has scroll to bottom
                        System.out.println("the given element does not exist");
                        deleteDiffImages();
                        return false;
                    }
                }

                saveScreenshot(beforePng);
                drag(startX, startY, endX, endY, 1);
                Thread.sleep(1000);
                saveScreenshot(afterPng);
            }
            deleteDiffImages();
            return true;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            deleteDiffImages();
            e.printStackTrace();
        }

        deleteDiffImages();
        return false;
    }

    /**
     * 滑动到最底部
     * 只支持Native页面
     */
    public void scrollToBottom() throws Exception {

        scrollToBottomOrTop(true);
    }

    /**
     * 滑动到最顶部
     * 只支持Native页面
     */
    public void scrollToTop() throws Exception {

        scrollToBottomOrTop(false);

    }

    /**
     * 滑动到最底部或最顶部
     *
     * @param isToBottom 是否滑动到最底部，true：滑动到最底部，false：滑动到最顶部
     */
    public void scrollToBottomOrTop(boolean isToBottom) throws Exception {
        JSONObject windowSize;
        try {
            windowSize = getWindowSize();
            int windowWidth = windowSize.getIntValue("width");
            int windowHeight = windowSize.getIntValue("height");

            int startX = 0, endX = 0, startY = 0, endY = 0;

            String beforeScreenShot = null, afterScreenShot = null;
            String beforePng = "before.png", afterPng = "after.png";

            //滑动到最底部
            if (isToBottom) {
                startX = windowWidth - 20;
                endX = startX;
                startY = windowHeight * 4 / 5;
                endY = windowHeight * 2 / 5;

            } else {
                //滑动到最顶部
                startX = windowWidth - 20;
                endX = startX;
                startY = windowHeight * 2 / 5;
                endY = windowHeight * 4 / 5;

            }
            deleteDiffImages();
            int flag = 15;
            while (flag > 0) {

                File shotOne = new File(beforePng);
                File shotTwo = new File(afterPng);
                beforeScreenShot = macaca.java.biz.BaseUtils.getFileMD5(shotOne);
                afterScreenShot = macaca.java.biz.BaseUtils.getFileMD5(shotTwo);
                if (beforeScreenShot != null
                        && beforeScreenShot.length() > 0) {
                    if (beforeScreenShot.equals(afterScreenShot)) {
                        // the same screen image ,it means current view has scroll to bottom or top
                        if (isToBottom) {
                            System.out.println("scrollToBottom");
                        } else {
                            System.out.println("scrollToTop");
                        }

                        deleteDiffImages();
                        return;
                    }
                }

                saveScreenshot(beforePng);
                drag(startX, startY, endX, endY, 1);
                Thread.sleep(1000);
                saveScreenshot(afterPng);
                // 为防止死循环，最多滑动10次
                flag--;
            }
            deleteDiffImages();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            deleteDiffImages();
            e.printStackTrace();
            macaca.java.biz.ResultGenerator.catchedException(e);
        }

        deleteDiffImages();
    }


    private void deleteDiffImages() throws Exception {
        try {
            // 如果存在图片，则删除,防止污染图片
            File shotOne = new File(BEFORE_IMAGE_NAME);
            File shotTwo = new File(AFTER_IMAGE_NAME);
            if (shotOne.exists()) {
                shotOne.delete();
            }

            if (shotTwo.exists()) {
                shotTwo.delete();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            ResultGenerator.catchedException(e);
        }

    }


    // App操作相关

    /**
     * Support : Android only
     * 启动app- 需在server启动后调用，如server未启动，需要通过BaseUtils.startApp(deviceId, packageName, activityName)调用
     * Support : Android Only
     */
    public void startApp() throws Exception {
        JSONObject capabilities = contexts.getCapabilities();
        JSONObject value = capabilities.getJSONObject("value");
        String deviceId = value.getString("udid");
        String packageName = value.getString("package");
        String activityName = value.getString("activity");
        macaca.java.biz.BaseUtils.startApp(deviceId, packageName, activityName);
    }

    /**
     * Support : Android Only
     * 清理app,回到初始状态 -需在server启动后调用，如server未启动，需要通过BaseUtils.clearApp(deviceId, packageName)调用
     */
    public void clearApp() throws Exception {

        JSONObject capabilities = contexts.getCapabilities();
        JSONObject value = capabilities.getJSONObject("value");
        String deviceId = value.getString("udid");
        String packageName = value.getString("package");
        macaca.java.biz.BaseUtils.clearApp(deviceId, packageName);
    }

    /**
     * Support : Android Only
     * 杀死app -需在server启动后调用，如server未启动，需要通过BaseUtils.forceStopApp(deviceId, packageName)调用
     */
    public void forceStopApp() throws Exception {
        JSONObject capabilities = contexts.getCapabilities();
        JSONObject value = capabilities.getJSONObject("value");
        String deviceId = value.getString("udid");
        String packageName = value.getString("package");
        BaseUtils.forceStopApp(deviceId, packageName);
    }


    // switch to the context of the last pushed webview

    /**
     * 从native切换到webview，更新上下文为当前最顶部的context
     * 当webview内部发生到另一个webview的跳转后，也可以调用此API，此时可以将上下文更新为最顶部的webview
     * (webview如果存在多个时，会存在多个上下文，此时要对当前可见的webview操作，需要将上下文更新为当前顶部的webview,可以通过此API实现)
     *
     * @return
     * @throws Exception 抛出异常
     */
    public void switchFromNativeToWebView() throws Exception {
        JSONArray contexts = contexts();
        this.context(contexts.get(contexts.size() - 1).toString());
    }

    /**
     * 当存在多个上下文时，切换当前上下文为当前最顶部的window，当webview发生跳转时可以通过此API更新上下文
     *
     * @throws Exception
     */
    public void updateTopContext() throws Exception {
        JSONArray contexts = contexts();
        this.context(contexts.get(contexts.size() - 1).toString());
    }

    /**
     * 从webview切换到native，实际上是取最底部的上下文，一般为Native上下文
     *
     * @return
     * @throws Exception 抛出异常
     */
    public void switchFromeWebviewToNative() throws Exception {
        JSONArray contexts = contexts();
        context(contexts.get(0).toString());
    }


}




