/* RixinClassQuery
 * @author            Phantrax
 * @version           1.0.0
 * @last-time         2015.8.20
 */


$(document).ready(function(){

    // Materialize
    $('.button-collapse').sideNav({
        menuWidth: 300, // Default is 240
        edge: 'right', // Choose the horizontal origin
        closeOnClick: true // Closes side-nav on <a> clicks, useful for Angular/Meteor
        }
    );
    var colWidth = $('.tab a').width();
    console.log(colWidth)
    $('.tabs .indicator').css('background', '#009688');
    $('.row .col').css('padding', '0');

    $('.indicator').css('width', colWidth);
    $('.class-table').css({'margin-top': '0', 'border-top': '0'});

    $('.class-name').parent().css('padding-left', colWidth+14);
    $('.class-num').css('width', colWidth);

    $('.class-info').css('padding-left', colWidth);
    $('.class-td').css({'height': '28px', 'padding': '7px 14px 7px 7px'});





    var date = new Date(),
        weekday;
    date.getDay() === 0 ? weekday = 7: weekday = date.getDay();
    // console.log(date, weekday);

    weekday = 2;
    var id = "20142110010101"; // window.interface.getStudentId()


    // $(".month").text(date.getMonth()+1);
    // $(".day").text(date.getDate());

    $.ajax({
        url: 'http://api.ecjtu.net/api.php',
        type: 'GET',
        dataType: 'jsonp',
        data: {classID: id},
    })
    .success(function (obj) {
        $('.class-date').each(function(index) {
            var i = index + 1;
            console.log($(this).children('ul.class-table').children('li').children('.collapsible-header').children('.class-name'));
            $(this).children('ul.class-table').children('li').children('.collapsible-header').children('.class-name').each(function(j) {
                var classStr = obj['content'][i][j];
                var ClassStrSliceObj = classStrSlice(classStr);
                if (obj['content'][i][j] === " ") {
                    $(this).text("").addClass('no-class');
                } else {
                    $(this).text(ClassStrSliceObj.Name);
                    // console.log($(this).parent().siblings().children('.class-info').children('.class-room'));
                    $(this).parent('.collapsible-header').siblings('.collapsible-body').children('.class-info').children('.class-td').children('.class-room').text(ClassStrSliceObj.Room);
                    $(this).parent('.collapsible-header').siblings('.collapsible-body').children('.class-info').children('.class-td').children('.class-time').text(ClassStrSliceObj.Time);
                    $(this).parent('.collapsible-header').siblings('.collapsible-body').children('.class-info').children('.class-td').children('.class-period').text(ClassStrSliceObj.Period);
                    $(this).parent('.collapsible-header').siblings('.collapsible-body').children('.class-info').children('.class-td').children('.class-teacher').text(ClassStrSliceObj.Teacher);
                }
            });
        });

    })
})

function classStrSlice (str) {
    var ClassStrSliceObj = {
        Name: "",
        Teacher: "",
        Room: "",
        Period: "",
        Time: "",
    }
    var count = 0;
    for (var i = 0; i < str.length; i++) {
        // console.log(str.charAt(i))
        if (str.charAt(i) == " " || str.charAt(i) == "\n") {
            count++;
            continue;
        }
        switch (count) {
            case 1:
                ClassStrSliceObj.Name += str.charAt(i);
                break;
            case 2:
                ClassStrSliceObj.Teacher += str.charAt(i);
                break;
            case 3:
                ClassStrSliceObj.Room += str.charAt(i);
                break;
            case 4:
                ClassStrSliceObj.Period += str.charAt(i);
                break;
            case 5:
                ClassStrSliceObj.Time += str.charAt(i);
        }
    };
    return ClassStrSliceObj;
}
