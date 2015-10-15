/* RixinScoreQuery
 * @author            Phantrax
 * @version           1.0.0
 * @last-time         2015.8.16
 */


$(document).ready(function() {
    $('select').material_select();

    var TermList = [
        ["2012-2013学年 第一学期", 2012.1],
        ["2012-2013学年 第二学期", 2012.2],
        ["2013-2014学年 第一学期", 2013.1],
        ["2013-2014学年 第二学期", 2013.2],
        ["2014-2015学年 第一学期", 2014.1],
        ["2014-2015学年 第二学期", 2014.2],
        ["2015-2016学年 第一学期", 2015.1],
        ["2015-2016学年 第二学期", 2015.2],
    ]

    // 初始化查询按钮位置
    // var initialBtn = setInterval(function () {
    //     var button = $("#get-score"),
    //         container = $(".container");
    //     // console.log(container.width(), button.width());
    //     var leftRem = (container.width() - button.width())/200 + "rem";
    //     // console.log(leftRem);
    //     button.css('left', leftRem);
    // }, 16);

    var scorePolling = setInterval(scoreQuery(), 16);

    function scoreQuery() {
        var score = $('#student_id').val();
        var term = $('.select-dropdown').val();
        // console.log(score term);
        var temp;
        for (var i = 0; i < TermList.length; i++) {
            if (TermList[i][0] == term) {
                temp = TermList[i][1];
                // console.log(temp);
            };
        }
        var URLRixin = "http://api.ecjtu.net/score.php", // 日新API
            URLEcjtu = "http://jwc.ecjtu.jx.cn/mis_o/cj.php?sid="; // 学校API
        $.ajax({
            url: URLRixin,
            type: 'GET',
            dataType: 'jsonp',
            data: {s: score},
            success: function (json) {
                var count = 0;
                $('ul.collection').empty()
                    .append('<li class="collection-header"><h6 class="teal-text text-darken-2">成績明細</h6></li>')

                $.each(json, function (i, scoreInfo) {
                    if (scoreInfo.Term == temp) {
                        $('#student_name').val(scoreInfo.Name);
                        $('ul.collection').append("<li class='collection-item row'><span class='score-course col s10'>" + scoreInfo.Course + "</span><span class='score-mark col s2 center-align'>" + scoreInfo.Score + "</span></li>");
                        $('.collection-item').css('padding', '0px');
                        $('.score-course').css('border-right', '1px solid #ddd');
                        $('.score-mark').css('height', $(this).siblings('.score-course').height());
                        count++;
                    }
                })
                if (count == 0) {
                    $("ul.collection").empty()
                        .append("<p class='empty'> 没有查到成绩咕OuO~ </p>");
                };
                // 不及格成绩标记
                $('span.score-mark').each(function() {
                    if ($(this).text() < 60 || $(this).text() == "不及格" || $(this).text() == "不合格") {
                        // console.log("X");
                        $(this).css('background', '#f96')
                            .prev().css('background', '#f96');
                    };
                });
            }
        });

    };
})
