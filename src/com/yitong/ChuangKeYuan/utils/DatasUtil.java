package com.yitong.ChuangKeYuan.utils;

import android.content.Context;
import android.widget.ImageView;

import com.yitong.ChuangKeYuan.Application.MyApplication;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.domain.AllUser;
import com.yitong.ChuangKeYuan.domain.DataPublicBean_Index;
import com.yitong.ChuangKeYuan.domain.DataPublicBean_Qita;
import com.yitong.ChuangKeYuan.domain.Recommend;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FBL
 * @ClassName: DatasUtil
 * @Description: TODO(此类为数据来源类)
 * @date 2016-07-05  14:30:00
 */
public class DatasUtil {
    /**
     * 网络请求之公共接口数据
     */
    public DatasUtil(){}
//    public static void setUrlBase(String urlBase) {
//        URL_BASE = urlBase;
//    }
    /**
     * 根接口http://192.168.1.111:18980/
     */
//    public static String URL_BASE = "http://192.168.3.9:18980/index.php/Webapi";

    /**
     * 新APP下载地址；
     */
    public static String URL_NewApp = "http://222.72.139.156:18980/public/software/ChuangKeYuan.apk";
    public static String URL_BASE = "http://222.72.139.156:18980/index.php/Webapi";

//    public static String URL_NewApp = "http://119.147.115.203:18980/public/software/ChuangKeYuan.apk";
//    public static String URL_BASE = "http://119.147.115.203:18980/index.php/Webapi";
//    public static String URL_BASE ;

    /**
     * 公开部分非推荐中的数据接口
     */
    public static String URL_qita = "/Index/termTree/";
    /**
     * 公开部分推荐中的部分数据接口
     */
    public static String URL_index = "/Index/index";

    /**
     * 文章列表数据接口
     */
    public static String URL_article = "/Article/articleList/";
    /**
     * 文章上传视频图片固定接口
     */
    public static String URL_video_phone = "http://119.147.115.203:18980/public/images/video.png";
    /**
     * 文章上传音频图片固定接口
     */
    public static String URL_audio_phone ="http://119.147.115.203:18980/public/images/video2.png";
    /**
     * 文章上传文件图片固定接口
     */
    public static String URL_file_phone ="http://119.147.115.203:18980/public/images/down.jpg";
    /**
     *文章修改
     */
    public static String URL_Editarticle = "/Article/articleEdit/";

    /**
     * 进行中的任务
     */
    public static String URL_TAskOnWay = "/Article/myfinishtaskList/";

    /**
     * 文章详情数据接口
     */
    public static String URL_article_details = "/Article/articleInfo/";

    /**
     * 家庭工作坊 删除；
     */
    public static String URL_article_del = "/Article/articleDel/";

    /**
     * 用户登录接口
     */
    public static String URL_login = "/Index/login/";

    /**
     * 用户退出接口
     */
    public static String URL_logout = "/Index/logout/";

    /**
     * 添加评论接口
     */
    public static String URL_AddComment = "/Article/addComment/";
    /**
     * 添加老师评价
     */
    public static String URL_AddTeacrherComment = "/Article/addGrade";

    /**
     * 获取已完成任务接口
     */
    public static String URL_TaskFinish = "/Article/myfinishtaskList/";
    /**
     * 获取所有任务
     */
    public static String URL_TaskAll = "/Newapi/articleList/";
    /**
     * 删除文物附件接口；
     */
    public static String URL_TaskFinishDelete = "/Article/delHomework";
    /**
     * 完成任务详情接口
     */
    public static String URL_Taskinfo = "/Article/finishtaskInfo";

    /**
     * 班级介绍接口;
     */
    public static String URL_classInfo = "/Article/classInfo/";

    /**
     * 综合评价接口;
     */
    public static String URL_Comment = "/Newapi/semester/";

    /**
     * 文章搜索关键字排行接口
     */
    public static String URL_getSearchKeyword = "/Article/getSearchKeyword/";

    /**
     * 文章搜索接口
     */
    public static String URL_articleSearch = "/Article/articleSearch/";

    /**
     * 导师列表
     */
    public static String URL_teacherList = "/Newapi/articleList/";
    /**
     * 班级导师详情
     */
    public static String URL_classteacherList = "/Article/teacherInfo/";
    /**
     * 外面导师详情
     */
    public static String URL_TutorteacherList = "/Newapi/TeacherInfo/";
    /**
     * 导师家长详情
     */
    public static String URL_teacherInfo = "/Newapi/articleInfo";
    /**
     * 添加导师
     */
    public static String URL_Addteacher = "/Newapi/addTeacher/";
    /**
     * 修改导师
     */
    public static String URL_Editteacher = "/Newapi/saveTeacher/";
    /**
     * 班级导师列表
     */
    public static String URL_class_teacherList = "/Article/teacherList/";
    /**
     * 添加家长
     */
    public static String URL_Addparents = "/Newapi/addArticle/";
    /**
     * 编辑家长
     */
    public static String URL_Editparents = "/Newapi/saveArticle/";
    /**
     * 家长列表
     */
    public static String URL_parentsList = "/Newapi/articleList/";
    /**
     * 收藏接口
     */
    public static String URL_Articlefavorites = "/Article/favorites/";
    /**
     * 收藏列表接口
     */
    public static String URL_ArticlefavoritesList = "/Article/favoritesList/";
    /**
     * 添加任务接口
     */
    public static String URL_addTAsk = "/Newapi/addTask/";

    /**
     * 修改任务接口
     */
    public static String URL_editTAsk = "/Newapi/editTask/";

    /**
     * 调查问卷接口
     */
    public static String URL_getQuestion = "/Newapi/getQuestionsList/";
    /**
     * 添加调查问卷接口
     */
    public static String URL_addQuestion = "/Newapi/addQuestions";
    /**
     * 提交调查问卷结果接口
     */
    public static String URL_upAnswer = "/Newapi/questionAnswer/";
    /**
     * 删除调查问卷结果接口
     */
    public static String URL_delAnswer = "/Newapi/deleteQuestion/";
    /**
     * 添加文章接口（包含上传媒体流）
     */
    public static String URL_AddArticle = "/Articlenew/addArticle";
    /**
     * 发现模块发布接口
     */
    public static String URL_Publish = "/Moments/publish/";
    /**
     * 我的模块累计发帖数量接口
     */
    public static String URL_Statistics = "/Index/myStatistics";
    /**
     * 我的模块记录在线时间接口
     */
    public static String URL_onlinetimeStatistics = "/Index/onlinetimeStatistics/";
    /**
     * 我的模块获取在线时间接口
     */
    public static String URL_recordTime = "/Index/recordTime/";

    /**
     * 发现圈数据；
     */
    public static String URL_FindData = "/Moments/getContent2";

    /**
     * 添加发现评论接口；
     */
    public static String URL_addComments = "/Moments/addComments/";

    /**
     * 删除发现内容（自己发布的）接口；
     */
    public static String URL_delPublish = "/Moments/delPublish/";

    /**
     * 删除发现评论（自己发布的）接口；
     */
    public static String URL_delComments = "/Moments/delComments/";

    /**
     * 点赞接口；
     */
    public static String URL_Addpraise = "/Moments/praise/";

    /**
     * 用户修改头像接口；
     */
    public static String URL_upAvatar = "/Index/modifiUserImgBase64";
    /**
     * 获取用户头像接口；
     */
    public static String URL_getHeadImg = "/Index/getHeadImg/";

    /**
     * 家长介绍修改接口；
     */
    public static String URL_editDesc = " /Index/editDesc/";

    /**
     * 添加作业附件并提交接口；
     */
    public static String URL_addHomework = "/Article/addHomework/";
    /**
     * 异常处理之上传log接口；
     */
    public static String URL_devicelog = "/Index/devicelog/";

    /**
     * 检查更新新版本以及检验服务器是否正常；
     */
    public static String URL_getversions = "/Index/versions";
    /**
     * 用户修改密码接口
     */
    public static String URL_ChangePassword = "/Index/modifiUserPwd/";

    /**
     * 发现消息
     */
    public static String URL_Message_Find = "/Moments/isUpdate";

    /**
     * 公开部分非推荐中的数据
     */
    public List<DataPublicBean_Qita.ListBean> mListBeen;

    /**
     * 公开部分推荐中的部分数据
     */
    public DataPublicBean_Index Been_index;

    /**
     * 所有用户的信息数据
     */
    public AllUser.ListBean UserlistBean;

    public AllUser.ListBean getUserListBean() {
        return UserlistBean;
    }

    public void setUserListBean(AllUser.ListBean listBean) {
        this.UserlistBean = listBean;
    }


    /**
     * 获取RecommendFragment 中的非ViewPager得图片和文本数据；
     */
    public static ArrayList<ArrayList<Recommend>> getRecomListViewData() {

//        if (MyApplication.getUtil().Been_index != null &&
//                Integer.parseInt(MyApplication.getUtil().Been_index.getCode()) == 0) {
        try {
            Recommend recommend = null;
            ArrayList<ArrayList<Recommend>> mArrayList = new ArrayList<ArrayList<Recommend>>();
            for (int i = 0; i < 4; i++) {
                ArrayList<Recommend> mList_gridview = new ArrayList<Recommend>();
                if (i == 0 | i == 2) {
                    if (i == 0) {
                        for (int j = 0; j < MyApplication.getUtil().Been_index.getJianshe().size(); j++) {
                            recommend = new Recommend();
                            recommend.setTitle(MyApplication.getUtil().Been_index.getJianshe().get(j).getTitle());
                            recommend.setImage(MyApplication.getUtil().Been_index.getJianshe().get(j).getThumb());
                            mList_gridview.add(recommend);
                        }
                    } else {
                        for (int j = 0; j < MyApplication.getUtil().Been_index.getKongjian().size(); j++) {
                            recommend = new Recommend();
                            recommend.setTitle(MyApplication.getUtil().Been_index.getKongjian().get(j).getTitle());
                            recommend.setImage(MyApplication.getUtil().Been_index.getKongjian().get(j).getThumb());
                            mList_gridview.add(recommend);
                        }
                    }
                } else if (i == 1) {
                    for (int j = 0; j < MyApplication.getUtil().Been_index.getDaoshi().size(); j++) {
                        recommend = new Recommend();
                        recommend.setTitle(MyApplication.getUtil().Been_index.getDaoshi().get(j).getUser_nicename());
                        recommend.setImage(MyApplication.getUtil().Been_index.getDaoshi().get(j).getThumb());
                        mList_gridview.add(recommend);
                    }
                } else {
                    for (int j = 0; j < MyApplication.getUtil().Been_index.getZuozhongxue().size(); j++) {
                        recommend = new Recommend();
                        recommend.setTitle(MyApplication.getUtil().Been_index.getZuozhongxue().get(j).getTitle());
                        recommend.setImage(MyApplication.getUtil().Been_index.getZuozhongxue().get(j).getThumb());
                        mList_gridview.add(recommend);
                    }
                }
                mArrayList.add(mList_gridview);
            }
            return mArrayList;
        }catch (Exception e){
            System.exit(0);
            return null;
        }
    }


    /**
     * MyFragment中的图片数据；
     */
    public static final int[] MY_PHOTO = {R.drawable.em_default_avatar};
    /**
     * MyFragment中的文本数据；
     */
    public static final String[] MY_TEXT = {"", "在线时间统计", "论坛发帖统计", "我的收藏","个人设置","关于创客园","当前版本"};
    /**
     * 引导界面
     */
    public static int[] GUIDE_PHOTO = {R.drawable.guide_1,
            R.drawable.guide_2, R.drawable.guide_3};
    /**
     * 引导界面
     */
    public static List<ImageView> getGuideData(Context context) {

        List mImageViews = new ArrayList<ImageView>();
        for (int i = 0; i < GUIDE_PHOTO.length; i++) {
            // 创建Imageview
            ImageView imageView = new ImageView(context);
            // 将图片设置给imageview
            imageView.setBackgroundResource(GUIDE_PHOTO[i]);
            // 将创建的imageview存放到集合或者数组中
            mImageViews.add(imageView);
        }
        return mImageViews;
    }

}
