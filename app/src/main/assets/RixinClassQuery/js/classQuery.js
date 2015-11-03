/* RixinClassQuery
 * @author            Phantr4x
 * @version           1.0.0
 * @last-time         2015.8.20
 */


$(document).ready(function(){
    
    // Materialize
    $('.button-collapse').sideNav({
        menuWidth: 300, // Default is 240
        edge: 'right', // Choose the horizontal origin
        closeOnClick: true // Closes side-nav on <a> clicks, useful for Angular/Meteor
    });
    var colWidth = $('.tab a').width();
    // console.log(colWidth)
    $('.tabs .indicator').css('background', '#009688');
    $('.row .col').css('padding', '0');

    $('.indicator').css('width', colWidth);
    $('.class-table').css({'margin-top': '0', 'border-top': '0'});

    $('.class-name').parent().css('padding-left', colWidth+14);
    $('.class-num').css('width', colWidth);

    $('.class-info').css('padding-left', colWidth);
    $('.class-td').css({'height': '28px', 'padding': '7px 14px 7px 7px'});
    $('.class-td, .class-room, .class-time, .class-period, .class-teacher').css('font', '1rem/1.2rem Roboto, Microsoft Yahei, sans-serif');

    var date = new Date(),
        weekday;
    date.getDay() === 0 ? weekday = 7: weekday = date.getDay();
    // console.log(date, weekday);

    /**
     * 初始化日期頁面顯示
     */
    $(document).find('li.tab').each(function(index) {
        // console.log(index+1)
        if (index + 1 == weekday) {$(this).children('a').click()};
    });

    /**
     * 滑動切換日期模塊
     */
    var startX, startY,
        endX, endY,
        X, Y,
        windowWidth = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth,
        windowHeight = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
    $(document).bind('touchstart', function(event) {
        startX = event.originalEvent.changedTouches[0].clientX;
        startY = event.originalEvent.changedTouches[0].clientY;
        // console.log(startX, startY);
    });
    $(document).bind('touchend', function(event) {
        endX = event.originalEvent.changedTouches[0].clientX;
        endY = event.originalEvent.changedTouches[0].clientY;
        X = endX - startX;
        Y = endY - startY;
        // console.log(endX, endY);
        // console.log(X, Y)
        // console.log(X / windowWidth)
        if (X / windowWidth > 0.25) {
            $(document).find('ul.tabs').find('a.active').parent('li').prev().children('a').click();
        } else if (X / windowWidth < -0.25) {
            $(document).find('ul.tabs').find('a.active').parent('li').next().children('a').click();
        }
    });


    // document.addEventListener("touchmove", function(event){
    //     console.log(event.changedTouches[0].clientX);
    // }, false);

    
    window.jsonp = function(json) {
        // console.log(json);
        json = json&&json[0];
        $('.class-date').each(function(index) {
            // console.log(index);
            var count = 0;
            for (var i in json) {
                // console.log(i)
                if (i % 7 === index + 1) {
                    count ++;
                    // console.log(i, index, count, json[i]);
                    // 课程名称
                    $(this)
                        .find('.class-name').eq(count-1)
                        .text(json[i].name == "null" ? "" : json[i].name);
                    // 课程教室
                    $(this)
                        .find('.class-info').eq(count-1)
                        .find('.class-room')
                        .text(json[i].room == "null" ? "" : json[i].room);
                    // 课程时间
                    $(this)
                        .find('.class-info').eq(count-1)
                        .find('.class-time')
                        .text(json[i].time == "null" ? "" : json[i].time);
                    // 课程周数    
                    $(this)
                        .find('.class-info').eq(count-1)
                        .find('.class-period')
                        .text(json[i].week == "null" ? "" : json[i].week);
                    // 任课教师
                    $(this)
                        .find('.class-info').eq(count-1)
                        .find('.class-teacher')
                        .text(json[i].teacher == "null" ? "" : json[i].teacher);
                };
            };
        });
        // console.log($(document).find('.class-name'))
        $(document).find('.class-name').each(function(index) {
            if($(this).text() !== "") {
                // console.log($(this).parents('.collapsible-header, li'))
                $(this).parents('.collapsible-header, li').addClass('active');
                $(this).parents('.collapsible-header').siblings('.collapsible-body').css('display', 'block');
            }
        });
    };    
    var id = window.rixin.getStudentId();
    $.ajax({
        url: 'http://api.ecjtu.net/class.php',
        type: 'GET',
        cache:true,
        dataType: 'jsonp',
        data: {num: id},
        jsonp:false,
        jsonpCallback:jsonp,
        success: function(json){
        },
    });
})
//var jsonp = function(json){console.log(json);}