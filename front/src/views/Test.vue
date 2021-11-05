
<template>
  测试
  <div class="qrcode" ref="qrCodeUrl"></div>
</template>

<script>
import QRCode from 'qrcodejs2'
export default {
  data() {
    return {
      qrcode: null,
    };
  },
  created() {
      
  },
  mounted() {
    this.creatQrCode();
  },
  methods: {
    creatQrCode() {
        var qrcode = new QRCode(this.$refs.qrCodeUrl, {
            text: "http://47.93.249.243:8090/questionnaire?questionnaireId=5&code=发布问卷成功",
            width: 200, // 二维码宽度，单位像素
            height: 200, // 二维码高度，单位像素
        })
        this.qrcode = qrcode;
    },
  },
};
</script>
<style scoped>

</style>