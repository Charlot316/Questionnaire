
// // 导出页面为PDF格式
// import html2Canvas from 'html2canvas'
// import JsPDF from 'jspdf'
// export function getPdf () {
//   var title = this.htmlTitle
//   html2Canvas(document.querySelector('#pdfDom'), {
//     allowTaint: true
//   }).then(function (canvas) {
//     let contentWidth = canvas.width
//     let contentHeight = canvas.height
//     let pageHeight = contentWidth / 592.28 * 841.89
//     let leftHeight = contentHeight
//     let position = 0
//     let imgWidth = 595.28
//     let imgHeight = 592.28 / contentWidth * contentHeight
//     let pageData = canvas.toDataURL('image/jpeg', 1.0)
//     let PDF = new JsPDF('', 'pt', 'a4')
//     if (leftHeight < pageHeight) {
//       PDF.addImage(pageData, 'JPEG', 0, 0, imgWidth, imgHeight)
//     } else {
//       while (leftHeight > 0) {
//         PDF.addImage(pageData, 'JPEG', 0, position, imgWidth, imgHeight)
//         leftHeight -= pageHeight
//         position -= 841.89
//         if (leftHeight > 0) {
//           PDF.addPage()
//         }
//       }
//     }
//     PDF.save(title + '.pdf')
//   }
//   )
// }

import html2canvas from 'html2canvas'
// import './jspdf.min.js'
import jsPDF from 'jspdf'
export function getPdf(title) {
  document.body.scrollTop = document.documentElement.scrollTop = 0
  // if (!selector) {
  //   throw new Error('缺少selector')
  // }
  let el = document.querySelector('#pdfDom')
  if (!el) {
    throw new Error('未找到' + selector + '对应的dom节点')
  }
  html2canvas(el, {
    allowTaint: true,
    useCORS: true,
    dpi: 120, // 图片清晰度问题
    background: 'white', //如果指定的div没有设置背景色会默认成黑色,这里是个坑
    height: el.scrollHeight,
    windowHeight: el.scrollHeight
  }).then(canvas => {
    //未生成pdf的html页面高度
    var leftHeight = canvas.height

    var a4Width = 595.28
    var a4Height = 841.89 //A4大小，210mm x 297mm，四边各保留10mm的边距，显示区域190x277
    //一页pdf显示html页面生成的canvas高度;
    var a4HeightRef = Math.floor((canvas.width / a4Width) * a4Height)

    //pdf页面偏移
    var position = 0

    var pageData = canvas.toDataURL('image/jpeg', 1.0)

    var pdf = new jsPDF('p', 'pt', 'a4') //A4纸，纵向
    var index = 1,
      canvas1 = document.createElement('canvas'),
      height
    pdf.setDisplayMode('fullwidth', 'continuous', 'FullScreen')

    var pdfName = title
    function createImpl(canvas) {
      if (leftHeight > 0) {
        index++

        var checkCount = 0
        if (leftHeight > a4HeightRef) {
          var i = position + a4HeightRef
          for (i = position + a4HeightRef; i >= position; i--) {
            var isWrite = true
            for (var j = 0; j < canvas.width; j++) {
              var c = canvas.getContext('2d').getImageData(j, i, 1, 1).data

              if (c[0] != 0xff || c[1] != 0xff || c[2] != 0xff) {
                isWrite = false
                break
              }
            }
            if (isWrite) {
              checkCount++
              if (checkCount >= 10) {
                break
              }
            } else {
              checkCount = 0
            }
          }
          height = Math.round(i - position) || Math.min(leftHeight, a4HeightRef)
          if (height <= 0) {
            height = a4HeightRef
          }
        } else {
          height = leftHeight
        }

        canvas1.width = canvas.width
        canvas1.height = height


        var ctx = canvas1.getContext('2d')
        ctx.drawImage(
          canvas,
          0,
          position,
          canvas.width,
          height,
          0,
          0,
          canvas.width,
          height,
        )

        var pageHeight = Math.round((a4Width / canvas.width) * height)
        // pdf.setPageSize(null, pageHeight)
        if (position != 0) {
          pdf.addPage()
        }
        pdf.addImage(
          canvas1.toDataURL('image/jpeg', 1.0),
          'JPEG',
          10,
          10,
          a4Width,
          (a4Width / canvas1.width) * height,
        )
        leftHeight -= height
        position += height
        if (leftHeight > 0) {
          setTimeout(createImpl, 500, canvas)
        } else {
          pdf.save(pdfName + '.pdf')
        }
      }
    }

    //当内容未超过pdf一页显示的范围，无需分页
    if (leftHeight < a4HeightRef) {
      pdf.addImage(
        pageData,
        'JPEG',
        0,
        0,
        a4Width,
        (a4Width / canvas.width) * leftHeight,
      )
      pdf.save(pdfName + '.pdf')
    } else {
      try {
        pdf.deletePage(0)
        setTimeout(createImpl, 500, canvas)
      } catch (err) {
       
      }
    }
  })
}
