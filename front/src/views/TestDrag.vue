<template>
   <!-- 拖拽交换位置效果 -->
	<transition-group tag="div" class="">
        <!-- 循环出三个颜色不同的div -->
        <div class="item" v-for="(item) in items" :key="item.key" 
        :style="{background:item.color,width:'80px',height:'80px'}"
            draggable="true"
            @dragstart="handleDragStart($event, item)"
            @dragover.prevent="handleDragOver($event, item)"
            @dragenter="handleDragEnter($event, item)"
            @dragend="handleDragEnd($event, item)" >
            <p class="p1">{{item.text}}</p>
        </div>
</transition-group>
</template>
 
<script>
// draggable 属性规定元素是否可拖动
// dragstart 事件在用户开始拖动元素或选择的文本时触发
// dragover 当某被拖动的对象在另一对象容器范围内拖动时触发此事件
// dragenter - 当被鼠标拖动的对象进入其容器范围内时触发此事件
// dragend - 用户完成元素拖动后触发
export default {
 name: 'Toolbar',
 data () {
  return {
   items: [
       {key: 1,text: "题目一"},
       {key: 2,text: '题目二'},
       {key: 3,text: '题目三'}

    // { key: 1, color: '#ffebcc'},
    // { key: 2, color: '#ffb86c'},
    // { key: 3, color: '#f01b2d'}
   ],
     
    dragging: null
  }
 },
 methods:{
  handleDragStart(e,item){
    this.dragging = item;
  },
  handleDragEnd(e,item){
    this.dragging = null
  },
  //首先把div变成可以放置的元素，即重写dragenter/dragover
  // DataTransfer 对象用来保存，通过拖放动作，拖动到浏览器的数据。
  // 如果dropEffect 属性设定为none,则不允许被拖放到目标元素中。
  handleDragOver(e) {
    e.dataTransfer.dropEffect = 'move'// e.dataTransfer.dropEffect="move";//在dragenter中针对放置目标来设置!
  },
  handleDragEnter(e,item){
    e.dataTransfer.effectAllowed = "move"//为需要移动的元素设置dragstart事件
    if(item === this.dragging){
      return
    }
    const newItems = [...this.items]
    const src = newItems.indexOf(this.dragging)
    const dst = newItems.indexOf(item)
    // 替换
    newItems.splice(dst, 0, ...newItems.splice(src, 1))
    // 让item的颜色等于新交换的颜色
    this.items = newItems
  }
 }
}
</script>
  
<style scoped>
  .container{
    width: 80px;
    height: 300px;
    position: absolute;
    left: 0;
    display:flex;
    flex-direction: column;
    padding: 0;
  }
  .item {
   margin-top: 10px;
   transition: all linear .3s;
   margin-left: 50%;
   border: 2px solid red;
   width: 10%;
   height: 10%;
  }
  .p1{
    margin-left: 10%;
    margin-top: 10%;
  }
</style>