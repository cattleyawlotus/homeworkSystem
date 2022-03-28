package com.forum;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.forum.Vo.BlogVo;
import com.forum.entity.*;
import com.forum.mapper.BlogMapper;
import com.forum.mapper.IpLocationMapper;
import com.forum.mapper.MessageMapper;
import com.forum.mapper.ViewsMapper;
import com.forum.service.*;
import com.forum.utils.PageUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ForumApplicationTests {

    //<editor-fold desc="Autowired">
    @Resource
    private RedisTemplate redisTemplate;
    @Autowired
    BlogService blogService;
    @Autowired
    CommentService commentService;
    @Autowired
    CrawledBlogService crawledBlogService;
    @Autowired
    ViewsService viewsService;
    @Autowired
    IpLocationService ipLocationService;
    @Autowired
    MessageService messageService;
    @Resource
    MessageMapper messageMapper;
    @Autowired
    UserService userService;
    @Autowired
    TypeService typeService;
    @Autowired
    TagService tagService;
    @Autowired
    BlogTagsService blogTagsService;
    @Autowired
    ThumbsService thumbsService;
    //</editor-fold>

    @Test
    public void test(){
        PageUtil<Blog> page = new PageUtil<>(blogService.getAllBlog(1,15));
        List<BlogVo> list=page.getList().stream().map(x->{
            BlogVo blogVo=new BlogVo();
            BeanUtils.copyProperties(x,blogVo);
            LambdaQueryWrapper<User> wrapper1=new LambdaQueryWrapper<>();
            wrapper1.eq(User::getId,x.getUid());
            User user=userService.getOne(wrapper1);
            if(user!=null) {
                blogVo.setUsername(user.getUsername());
                blogVo.setAvatar(user.getAvatar());
            }
            LambdaQueryWrapper<Type>wrapper2=new LambdaQueryWrapper<>();
            wrapper2.eq(Type::getId,x.getTypeId());
            Type type=typeService.getOne(wrapper2);
            if(type!=null) {
                blogVo.setType(type.getTypeName());
            }
            LambdaQueryWrapper<BlogTags>wrapper3=new LambdaQueryWrapper<>();
            wrapper3.eq(BlogTags::getBlogId,x.getId());
            List<BlogTags>blogTagsList=blogTagsService.list(wrapper3);
            List<Tag>tagList=new ArrayList<>();
            for(BlogTags blogTags:blogTagsList){
                LambdaQueryWrapper<Tag>wrapper4=new LambdaQueryWrapper<Tag>();
                wrapper4.eq(Tag::getId,blogTags.getTagId());
                tagList.add(tagService.getOne(wrapper4));
            }
            blogVo.setTags(tagList);
            return blogVo;
        }).collect(Collectors.toList());


    }

    @Test
    public void TestModel1() {
        String text="本院认为:被告人符正玉违反道路交通运输管理法规，酒后驾驶机动车辆在道路上行驶，发生交通事故并致一人死亡，负事故的全部责任，且在交通肇事后逃逸，其行为已构成交通肇事罪。、公诉机关指控的事实和罪名成立，本院予以确认。、对辩护人辩护提出的被告人符正玉不构成交通肇事逃逸，应在三年以下量刑的意见，经查，“交通运输肇事后逃逸”,是指行为人在发生交通事故后,为逃避法律追究而逃跑的行为。、被告人符正玉虽拨打了报警电话，但其未在现场等待交警部门依法处理，而是让其岳父刘某1帮忙找人顶包，其本人离开现场到医院输液以达到稀释其血液中酒精含量逃避侦查的目的，符正玉以上行为均表明其主观上具有逃避法律追究的目的，客观上实施了离开事故现场的行为，由于被告人符正玉意志以外的原因未能找人顶包成功并不影响其交通肇事后逃逸的成立，故对辩护人关于符正玉不应认定为交通肇事后逃逸，建议在三年以下量刑的意见不予采纳。、被告人符正玉案发后主动投案，如实供述犯罪事实，系自首，可对其从轻处罚，对辩护人提出的被告人符正玉系自首，建议对其从轻处罚的意见予以采纳。、被告人符正玉不符合缓刑适用条件，故对辩护人提出的建议对其适用缓刑的意见不予采纳。依照《中华人民共和国刑法》第一百三十三条、第六十七条第一款，《最高人民法院关于审理交通肇事刑事案件具体应用法律若干问题的解释》第二条第一款第（一）项、第三条之规定，判决如下:、被告人符正玉犯交通肇事罪，判处有期徒刑三年十个月。、（刑期从判决执行之日起计算，判决执行以前先行羁押的，羁押一日折抵刑期一日，即自2019年1月20日起至2022年11月19日止。";
        String text2="本院认为:被告人韩吉应违反交通运输管理法规，驾驶机动车辆发生道路交通事故，致一人死亡，且负事故的全部责任，其行为已构成交通肇事罪。、公诉机关指控的犯罪事实清楚，证据确实、充分，罪名成立，本院予以支持。、事故发生后，被告人韩吉应的亲属拨打了急救、报警电话，被告人韩吉应在现场等候处理，后经公安机关民警口头传唤到案，如实供述自己罪行，且当庭自愿认罪，其行为构成自首，依法可以从轻处罚。、被告人韩吉应与被害人近亲属就道路交通事故人身损害赔偿等达成协议，且已履行，其行为已取得被害人近亲属的谅解，据此可酌情对被告人韩吉应予以从轻处罚。、被告人韩吉应到案后自愿如实供述自己罪行，承认指控的犯罪事实，愿意接受处罚，可以依法从宽处理。、其辩护人关于被告人韩吉应具有法定和酌定从轻处罚情节，建议对被告人韩吉应从轻处罚并适用缓刑的辩护意见，本院予以采纳。综上所述，根据被告人韩吉应的犯罪事实、性质、量刑情节及社会危害程度，依照《中华人民共和国刑法》第一百三十三条，第六十七条第一款，第七十二条第一款，第七十三条第二、三款；《中华人民共和国刑事诉讼法》第十五条之规定，判决如下:、被告人韩吉应犯交通肇事罪，判处有期徒刑一年，缓刑一年六个月。、（缓刑考验期限，从判决确定之日起计算。";

        String[] cmdArgs = new String[]{"python", "C:\\Users\\lzr\\Desktop\\root\\nlp\\事件要素抽取\\事件要素抽取.py",text};
//        String[] cmdArgs = new String[]{"python", "C:\\Users\\lzr\\Desktop\\root\\nlp\\判决结果预测\\run.py",text};
//        String[] cmdArgs = new String[]{"python", "C:\\Users\\lzr\\Desktop\\root\\nlp\\计算案件间相似度\\judgment_similarity.py",text};
        try {
            //执行这个.exe程序
            Process process = Runtime.getRuntime().exec(cmdArgs);// 执行py文件
            // 获取程序执行后返回的结果
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            //java代码中的process.waitFor()返回值为0表示我们执行.exe文件成功，
            //返回值为1表示执行.exe文件失败，这和我们通常意义上见到的0与1定义正好相反
            int re = process.waitFor();
            System.out.println(re);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
