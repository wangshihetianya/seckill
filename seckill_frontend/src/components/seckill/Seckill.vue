<template>
    <div>
        <!-- 面包屑导航 -->
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{path:'home'}">首页</el-breadcrumb-item>
            <el-breadcrumb-item>秒杀界面</el-breadcrumb-item>
            <el-breadcrumb-item>开始秒杀</el-breadcrumb-item>
        </el-breadcrumb>

        <div>
            <el-row v-for="row in rowNumber" :key="row">
                <el-col :span="4" v-for="item in activityList" :key="item" :offset="1">
                    <el-card :body-style="{ padding: '0px' }">
                    <img src="../../assets/seckill.jpg" class="image">
                    <div style="padding: 14px;">
                        <span>{{item.title}}</span>
                        <div class="bottom clearfix">
                        <time class="time">{{item.startTime}}</time>
                        <el-button type="text" class="button" @click="seckill(item)" :disabled="haveSeckill">抢购</el-button>
                        <el-button type="text" class="button" @click="result(item.id)">查看结果</el-button>
                        </div>
                    </div>
                    </el-card>
                </el-col>
            </el-row>
        </div>


    </div>
</template>
<script>
export default {
    created() {
        this.getActivity();
    },
    data() {
        return {
            //查询对象
            queryInfo: {
                query: "",
                pageNum: 1,
                pageSize: 5,
            },

            activityList:[],
            total: 0,   //总记录数

            rowNumber: 1,   //行数，我们每行放4个活动

            haveSeckill: false  //是否已经抢购过
        }
    },
    methods: {
        //获取活动
        getActivity() {
            this.$http.get('/activity',{params:this.queryInfo})
            .then(res => {
                console.log(res);
                console.log(res.data);
                console.log(res.data.flag);
                //正常返回
                if (res.data.flag == "ok") {
                    this.activityList = res.data.activityList;
                    this.total = res.data.activityCount;
                } else {
                    this.$message.error("获取活动失败！！！")
                }
            })
        },
        //抢购
        seckill(activity) {

            this.haveSeckill = true;

            this.$http({
                    method:"post",
                    url: "http://localhost:9000/seckill",
                    data: activity
                })
                .then(res => {
                    console.log(res);

                    if (res.data.flag != "ok") {
                        return this.$message.error("还未到抢购时间！");
                    } else {
                        //抢购时间到，去真正的url抢购
                        this.realSeckill(res.data.realURL, activity.id);
                    }
                })
        },
        //查看结果
        result(activityID) {
            this.$http({
                    method:"post",
                    url: "http://localhost:9000/result",
                    data: {
                        activityID: activityID,
                        userID: window.sessionStorage.getItem('userid')
                    },
                    // transformRequest: [function (data) {
                    //     return qs.stringify(data);
                    // }],
                    
                })
                .then(res => {
                    // console.log(res);
                    if (res.data.flag != "ok") {
                        return this.$message.error("抢购失败或者还没抢购！");
                    } else {

                        if (!res.data.isFinallySuccess) {
                            return this.$message.error("还在排队中，请稍安勿躁！");
                        }

                        return this.$message.success("抢购成功！");

                    }
                })
        },
        //真正的抢购
        realSeckill(realURL, activityID) {
            
            this.$http({
                    method:"post",
                    url: "http://localhost:9000/" + realURL,
                    data: {
                        activityID: activityID,
                        userID: window.sessionStorage.getItem('userid')
                    },
                    // transformRequest: [function (data) {
                    //     return qs.stringify(data);
                    // }],
                    
                })
                .then(res => {
                    // console.log(res);
                    if (res.data.flag != "ok") {

                        this.haveSeckill = false;
                        return this.$message.error("服务器出问题了！");
                    } else {

                        if (!res.data.isSeckillSuccess) {
                            this.haveSeckill = false;
                            return this.$message.error("抢购失败，商品已卖完！");
                        }

                        return this.$message.success("抢购成功！");

                    }
                })
        }
    },

}
</script>
<style scoped>
.el-row{
    margin-top: 20px;
}

.time {
    font-size: 13px;
    color: #999;
}

.bottom {
    margin-top: 13px;
    line-height: 12px;
}

.button {
    padding: 0;
    float: right;
    margin-left: 5px;
}

.image {
    width: 100%;
    display: block;
}

.clearfix:before,
.clearfix:after {
    display: table;
    content: "";
}

.clearfix:after {
    clear: both
}
</style>