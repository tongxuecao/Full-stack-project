<template>
  <div class="about-page">
    <div class="about-hero">
      <h1>帮助与防骗指南</h1>
      <p>安全交易，从了解规则开始</p>
    </div>

    <el-row :gutter="24">
      <el-col :span="16">
        <div class="section">
          <h2><el-icon><WarningFilled /></el-icon> 防骗指南</h2>
          <el-collapse accordion>
            <el-collapse-item title="1. 警惕低价陷阱" name="1">
              <p>如果某件商品的价格远低于市场价（例如全新手机只卖几百元），请提高警惕。骗子常常利用"急出""搬家甩卖"等话术来让你放松戒备。</p>
              <p><strong>建议：</strong>在购买前搜索同类商品的市场价格，对异常低价保持怀疑。</p>
            </el-collapse-item>
            <el-collapse-item title="2. 坚持校内当面交易" name="2">
              <p>千万不要轻信"先付款后发货"或者"付定金预留"的说辞。绝大多数校园交易诈骗都发生在线上付款环节。</p>
              <p><strong>建议：</strong>尽量选择在食堂、图书馆等校内公共区域见面交易，一手交钱一手交货。</p>
            </el-collapse-item>
            <el-collapse-item title="3. 核实对方身份" name="3">
              <p>交易前可以通过平台展示的学号信息确认对方是否为在校学生。如果对方拒绝提供学号或闪烁其词，请立即终止交易。</p>
              <p><strong>建议：</strong>在交易前要求对方在平台内发送消息确认身份，保留聊天记录作为凭证。</p>
            </el-collapse-item>
            <el-collapse-item title="4. 仔细检查商品实物" name="4">
              <p>购买电子产品时务必当场开机测试，检查外观是否有磕碰、屏幕是否有坏点、功能是否正常。购买书籍时检查是否有缺页、涂写。</p>
              <p><strong>建议：</strong>提前准备好测试方法（如手机拨号键输入 *#06# 查 IMEI），不要因为不好意思而跳过验货环节。</p>
            </el-collapse-item>
            <el-collapse-item title="5. 保护个人隐私" name="5">
              <p>不要将自己的学号、身份证号、银行卡号等敏感信息泄露给交易对象。如果交易需要联系方式，使用平台站内功能即可。</p>
              <p><strong>建议：</strong>警惕索要验证码、要求点击不明链接的行为，这些都是常见的诈骗手段。</p>
            </el-collapse-item>
            <el-collapse-item title="6. 遇到问题及时举报" name="6">
              <p>如果在交易过程中遇到可疑行为或已经遭受损失，请第一时间联系学校保卫处或拨打校园报警电话，同时在平台进行举报。</p>
              <p><strong>建议：</strong>截图保存所有聊天记录和交易凭证，这些是后续维权的重要证据。</p>
            </el-collapse-item>
          </el-collapse>
        </div>

        <div class="section">
          <h2><el-icon><QuestionFilled /></el-icon> 常见问题</h2>
          <el-collapse accordion>
            <el-collapse-item title="如何发布商品？" name="faq1">
              <p>登录后点击导航栏的"我要发布"按钮，填写商品标题、选择分类、上传图片、填写价格和描述信息后即可发布。图片支持 JPG/PNG 格式，最多上传 5 张。</p>
            </el-collapse-item>
            <el-collapse-item title="如何联系卖家？" name="faq2">
              <p>在商品详情页可以看到卖家的认证学号和联系方式（需登录后可见）。建议先在平台内通过学号确认对方身份，再约定线下见面交易。</p>
            </el-collapse-item>
            <el-collapse-item title="购买后如何确认交易完成？" name="faq3">
              <p>本平台目前为信息撮合平台，线上点击"立即购买"仅表示交易意向确认。实际付款和交货请在线下完成。购买后商品状态会自动更新为"已售出"。</p>
            </el-collapse-item>
            <el-collapse-item title="如何修改个人信息？" name="faq4">
              <p>进入"个人中心"，点击头像可以更换头像，点击用户名可以修改名称，点击"修改密码"可以更改登录密码。</p>
            </el-collapse-item>
            <el-collapse-item title="收藏的商品在哪里查看？" name="faq5">
              <p>在商品卡片右上角点击星形图标即可收藏，已收藏的商品可以在个人中心的"我的收藏"页面统一查看和管理。</p>
            </el-collapse-item>
          </el-collapse>
        </div>
      </el-col>

      <el-col :span="8">
        <div class="side-card">
          <h3><el-icon><Platform /></el-icon> 平台简介</h3>
          <p>校园二手交易平台致力于为在校学生提供一个安全、便捷、可靠的二手物品交易环境。</p>
          <el-divider />
          <p><el-icon><Check /></el-icon> 仅限在校学生使用</p>
          <p><el-icon><Check /></el-icon> 学号认证，身份透明</p>
          <p><el-icon><Check /></el-icon> 支持面对面安全交易</p>
          <p><el-icon><Check /></el-icon> 完全免费，无中间费用</p>
        </div>

        <div class="side-card">
          <h3><el-icon><Phone /></el-icon> 联系我们</h3>
          <p>如有任何问题或建议，欢迎通过以下方式联系我们：</p>
          <el-divider />
          <p><strong>反馈邮箱：</strong>support@campus-trade.cn</p>
          <p><strong>工作时间：</strong>工作日 9:00 - 18:00</p>
          <p><strong>紧急情况：</strong>请联系学校保卫处</p>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { WarningFilled, QuestionFilled, Platform, Check, Phone } from '@element-plus/icons-vue'
</script>

<style scoped>
.about-page {
  max-width: 1100px;
  margin: 0 auto;
  padding: 0 20px 40px;
}

.about-hero {
  text-align: center;
  padding: 48px 20px 36px;
}

.about-hero h1 {
  font-size: 28px;
  color: var(--el-text-color-primary);
  margin: 0 0 10px;
}

.about-hero p {
  font-size: 15px;
  color: var(--el-text-color-secondary);
  margin: 0;
}

.section {
  background: var(--el-bg-color);
  border-radius: 10px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 1px 6px rgba(0, 0, 0, 0.04);
  transition: background 0.3s;
}

.section h2 {
  font-size: 18px;
  color: var(--el-text-color-primary);
  margin: 0 0 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.section h2 .el-icon {
  color: var(--el-color-warning);
}

.section .el-collapse-item p {
  color: var(--el-text-color-regular);
  line-height: 1.8;
  margin: 0 0 6px;
}

.side-card {
  background: var(--el-bg-color);
  border-radius: 10px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 1px 6px rgba(0, 0, 0, 0.04);
  transition: background 0.3s;
}

.side-card h3 {
  font-size: 16px;
  color: var(--el-text-color-primary);
  margin: 0 0 12px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.side-card h3 .el-icon {
  color: var(--el-color-primary);
}

.side-card p {
  color: var(--el-text-color-regular);
  line-height: 2;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 6px;
}

.side-card p .el-icon {
  color: var(--el-color-success);
}
</style>
