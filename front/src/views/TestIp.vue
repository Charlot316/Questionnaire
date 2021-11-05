<template>
    <button @click="getLocation1()">确认定位</button>
    <button @click="print()">展示city</button>
    <p id="p">{{city2}}</p>
</template>

<script>
// import { location } from '../utils/Location'
export default {
  data() {
  // debugger
  return {
    info: {},
    info1: {},
    city2: "",
  };
 },
methods:{
  
 

  getLocation1 () {
      const self = this;
      AMap.plugin('AMap.Geolocation', function () {
        var geolocation = new AMap.Geolocation({
          // 是否使用高精度定位，默认：true
          enableHighAccuracy: true,
          // 设置定位超时时间，默认：无穷大
          timeout: 10000
        });
 
        geolocation.getCurrentPosition();
        AMap.event.addListener(geolocation, 'complete', onComplete);
        AMap.event.addListener(geolocation, 'error', onError);
 
        function onComplete (data) {
          // data是具体的定位信息
          self.province = data.addressComponent.province;
          self.city = data.addressComponent.city;
          self.district = data.addressComponent.district;
          self.location = self.province+" "+self.city+" "+self.district;
        }
 
        function onError (data) {
          // 定位出错
          // 调用IP定位
          self.getLngLatLocation();
        }
      });
    },


    print(){
      this.city2 = this.location;
    }

  
 
},
 created() {
  //  this.getLocatioon();
  //  this.getLocation1();
 }
 
}
</script>
<style scoped>



















































































































































</style>