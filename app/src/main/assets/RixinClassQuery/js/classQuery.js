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


    var json = {"24": {}, "30": {"week": "1-12", "time": "9,10", "room": "31-302", "name": "\u4e2d\u56fd\u8fd1\u73b0\u4ee3\u53f2\u7eb2\u8981", "teacher": "\u8212\u5efa\u56fd"}, "25": {}, "22": {}, "26": {}, "27": {}, "20": {}, "32": {}, "21": {}, "11": {"week": "1-12", "time": "3,4", "room": "null", "name": "\u5927\u5b66\u82f1\u8bedIII", "teacher": "null"}, "10": {"week": "1-16", "time": "3,4", "room": "31-403", "name": "\u6570\u636e\u5e93\u7cfb\u7edf\u539f\u7406 \uff08A\uff09", "teacher": "\u5468\u8389"}, "13": {}, "12": {"week": "1-16", "time": "3,4", "room": "31-319", "name": "\u6570\u636e\u5e93\u7cfb\u7edf\u539f\u7406 \uff08A\uff09", "teacher": "\u5468\u8389"}, "15": {"week": "1-8", "time": "5,6", "room": "31-404", "name": "\u8ba1\u7b97\u65b9\u6cd5 \uff08A\uff09", "teacher": "\u5411\u534e\u840d"}, "14": {}, "17": {"week": "1-16", "time": "5,6", "room": "null", "name": "\u4f53\u80b2III", "teacher": "null"}, "16": {"week": "1-16", "time": "5,6,7", "room": "31-317", "name": "Web \u5e94\u7528\u8bbe\u8ba1\u57fa\u7840\uff08A\uff09", "teacher": "\u8c22\u5251\u731b"}, "19": {}, "18": {"week": "9-11", "time": "5,6", "room": "31-202", "name": "\u5f62\u52bf\u653f\u7b56\u4e0e\u7701\u60c5\u6559\u80b22", "teacher": "\u96f7\u5a09\u5a77"}, "31": {}, "23": {"week": "1-16", "time": "5,6,7", "room": "31-317", "name": "Web \u5e94\u7528\u8bbe\u8ba1\u57fa\u7840\uff08A\uff09", "teacher": "\u8c22\u5251\u731b"}, "28": {}, "29": {}, "35": {}, "34": {}, "1": {"week": "1-16", "time": "1,2", "room": "31-307", "name": "Java\u7a0b\u5e8f\u8bbe\u8ba1(A)   ", "teacher": "\u9648\u6d77\u6797"}, "33": {}, "3": {"week": "1-16", "time": "1,2", "room": "31-321", "name": "\u6982\u7387\u8bba\u4e0e\u6570\u7406\u7edf\u8ba1", "teacher": "\u5ed6\u5b87\u6ce2"}, "2": {"week": "1-8", "time": "1,2", "room": "31-404", "name": "\u8ba1\u7b97\u65b9\u6cd5 \uff08A\uff09", "teacher": "\u5411\u534e\u840d"}, "5": {}, "4": {"name1": "\u6982\u7387\u8bba\u4e0e\u6570\u7406\u7edf\u8ba1", "teacher1": "\u5ed6\u5b87\u6ce2", "time1": "1,2", "time2": "1,2", "room2": "31-318", "name2": "\u8f6f\u4ef6\u5de5\u7a0b\uff08A\uff09", "room1": "31-213", "week1": "1-16[\u53cc]", "teacher2": "\u6c64\u6587\u4eae", "week2": "1-16[\u5355]"}, "7": {}, "6": {}, "9": {"week": "1-16", "time": "3,4", "room": "31-317", "name": "\u8f6f\u4ef6\u5de5\u7a0b\uff08A\uff09", "teacher": "\u6c64\u6587\u4eae"}, "8": {"week": "1-12", "time": "3,4", "room": "null", "name": "\u5927\u5b66\u82f1\u8bedIII", "teacher": "null"}}


    // var date = new Date(),
    //     weekday;
    // date.getDay() === 0 ? weekday = 7: weekday = date.getDay();
    // // console.log(date, weekday);
    //
    // weekday = 2;
    var id = "20142110011209"; // window.interface.getStudentId()


    // $(".month").text(date.getMonth()+1);
    // $(".day").text(date.getDate());
    $.ajax({
        url: 'http://api.ecjtu.net/class.php',
        type: 'GET',
        dataType: 'json',
        data: {num: id}
    })
    .success(function (obj) {
        /*$('.class-date').each(function(index) {
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
        });*/
        console.log(obj);
    })
})

/*function classStrSlice (str) {
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
}*/
