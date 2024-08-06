//사진 썸네일
function loadImg(obj) {
    //매개변수로 받은 객체는 input type="file" 인 요소
    //해당 객체의 files속성에 접근하면 첨부파일 정보를 볼 수 있음
    //files 속성은 파일의 multiple 옵션이 존재하기 때문에 여러개를 담을 수 있음
    //files는 배열과 유사한 형태이지만 배열이 아님(foreach 쓸 수 없음)
    //파일 갯수가 0개가 아니고 && 해당파일이 데이터가 존재하는 파일이라면
    if (obj.files.length != 0 && obj.files[0] != 0) {
        //첨부파일이 들어왔을때
        //첨부파일의 경로에는 접근이 불가능하기때문에 첨부파일 데이터를 읽어옴
        const reader = new FileReader();//파일정보를 읽어올 수 있는 js 객체
        //선택한 파일 정보를 읽어오는 함수
        reader.readAsDataURL(obj.files[0]);
        //파일리더가 정보를 다 읽어오고 나면 동작할 함수 작성
        reader.onload = function (e) {
            //=>리더가 정보를 다 읽어오면 동작함>>서버는 그동안 다른 일을 할 수 있음
            //읽어온 파일 정보가 매개변수에 들어있음
            $(obj).prev().prev().children("img").attr("src", e.target.result).show();
        }
    } else {
        //첨부파일이 사라졌을 때
        $(obj).prev().prev().attr("src", "").hide();
    }
}

//메뉴 삭제 버튼
const delBtn = $("table").find("div.btn-box").children(".btn-mono");
delBtn.on("click", function () { deleteTr(this) });
//메뉴 추가 버튼
const completeBtn = $("table").find("div.btn-box").children(".btn-sub");
completeBtn.on('click', function () {
    completeTr(this);
})

//메뉴 항목 추가 버튼
function insertTr() {
    const tr = $("<tr>");
    // 메뉴 이름
    const MenuNameTd = $("<td>");
    const MenuNameDiv = $("<div>");
    MenuNameDiv.addClass("input-box");
    const MenuNameInput = $("<input type='text' name='menuName' placeholder='메뉴 이름'>");
    MenuNameInput.addClass("input-td").addClass("round-box");
    MenuNameDiv.append(MenuNameInput);
    MenuNameTd.append(MenuNameDiv);
    // 가격
    const MenuPriceTd = $("<td>");
    const MenuPriceDiv = $("<div>");
    MenuPriceDiv.addClass("input-box");
    const MenuPriceInput = $("<input type='text' name='menuPrice' placeholder='가격'>");
    MenuPriceInput.addClass("input-td").addClass("round-box");
    const span = $("<span>");
    span.text("원");
    MenuPriceDiv.append(MenuPriceInput).append(span);
    MenuPriceTd.append(MenuPriceDiv);
    // 버튼
    const btnTd = $("<td>");
    const btnDiv = $("<div>");
    btnDiv.addClass("btn-box");
    const insertBtn = $("<button type='button'>");
    insertBtn.addClass("btn").addClass("btn-sm").addClass("btn-sub");
    insertBtn.text("추가");
    insertBtn.on("click", function () {
        completeTr(this);
    });
    const deleteBtn = $("<button type='button'>");
    deleteBtn.addClass("btn").addClass("btn-sm").addClass("btn-mono");
    deleteBtn.text("삭제");
    deleteBtn.on("click", function () {
        deleteTr(this);
    });
    btnDiv.append(insertBtn).append($("<br>")).append(deleteBtn);
    btnTd.append(btnDiv);
    // 합치기
    tr.append(MenuNameTd).append(MenuPriceTd).append(btnTd);
    // 추가
    const tbody = $("table.tbl").children("tbody");
    tbody.append(tr);
}
//메뉴 관련 함수 만들기
function deleteTr(btn) {
    const tr = $(btn).parents("tr");
    tr.remove();
}
function completeTr(btn) {
    const tds = $(btn).parents("tr").children();
    let isCorrect = true;
    tds.each(function (index, item) {
        if (index < 2) {
            if ($(item).find("input").val() == "") {
                alert("메뉴를 모두 입력하세요");
                isCorrect = false;
                return;
            }
        }
    });
    if (isCorrect) {
        tds.each(function (index, item) {
            if (index == 2) {
                $(item).html("<button type='button' class='btn btn-sm btn-mono' onclick='updateTr(this);'>수정</button>");
            }
            else if (index == 0) {
                const text = $(item).find("input").val();
                $(item).text(text);
                $(item).addClass("menuName");
            } else {
                const text = "<span class='menuPrice'>" + $(item).find("input").val() + "</span>" + "<span> 원</span>";
                $(item).html(text);
                $(item).addClass("menuPrice");
            }
        });

    }
};

function updateTr(btn) {
    const tr = $(btn).parents("tr");
    const MenuNameTd = $("<td>");
    const MenuNameDiv = $("<div>");
    MenuNameDiv.addClass("input-box");
    const MenuNameInput = $("<input type='text' name='menuName' placeholder='메뉴 이름'>");
    MenuNameInput.addClass("input-td").addClass("round-box").val(tr.children().eq(0).text());
    MenuNameDiv.append(MenuNameInput);
    MenuNameTd.append(MenuNameDiv);
    // 가격
    const MenuPriceTd = $("<td>");
    const MenuPriceDiv = $("<div>");
    MenuPriceDiv.addClass("input-box");
    const MenuPriceInput = $("<input type='text' name='menuPrice' placeholder='가격'>");
    MenuPriceInput.addClass("input-td").addClass("round-box").val(tr.children().eq(1).children().eq(0).text());
    const span = $("<span>");
    span.text("원");
    MenuPriceDiv.append(MenuPriceInput).append(span);
    MenuPriceTd.append(MenuPriceDiv);
    // 버튼
    const btnTd = $("<td>");
    const btnDiv = $("<div>");
    btnDiv.addClass("btn-box");
    const insertBtn = $("<button type='button'>");
    insertBtn.addClass("btn").addClass("btn-sm").addClass("btn-sub");
    insertBtn.text("추가");
    insertBtn.on("click", function () {
        completeTr(this);
    });
    const deleteBtn = $("<button type='button'>");
    deleteBtn.addClass("btn").addClass("btn-sm").addClass("btn-mono");
    deleteBtn.text("삭제");
    deleteBtn.on("click", function () {
        deleteTr(this)
    });
    btnDiv.append(insertBtn).append($("<br>")).append(deleteBtn);
    btnTd.append(btnDiv);
    // 합치기
    tr.empty();
    tr.append(MenuNameTd).append(MenuPriceTd).append(btnTd);
}
//태그 추가
$("#insertTag").on("click", function () {
    const tagText = $("#restrTag").val();
    if (tagText == "") {
        alert("태그를 입력해주세요");
        return;
    }
    const tagDiv = $("<div>");
    tagDiv.addClass("tag");
    const tagDivHtml = "<span class='tag-name'>" + tagText + "</span><span class='material-symbols-outlined' onclick='deleteTag(this);'>cancel</span>";
    tagDiv.html(tagDivHtml);
    $("div.tag-box").append(tagDiv);
    $("#restrTag").val("");
    //list에 넣는 작업 필요
})

//태그 삭제
const cancels = $("div.tag").children("span.material-symbols-outlined");

cancels.on("click", function () {
    deleteTag(this);
})

function deleteTag(span) {
    const delTag = $(span).parent();
    delTag.remove();
    //빼는 작업 따로 필요
}
//주소찾기
$("#addrSearch").on("click", function () {
    new daum.Postcode({
        oncomplete: function (data) {
            $("#restrAddr1").val(data.roadAddress);
            $("#restrAddr2").focus();
            searchAddressToCoordinate(data.roadAddress);
        }
    }).open();
})
//네이버 지도 띄워서 위치 표시, 클릭하면 주소 변경 가능하도록..
const map = new naver.maps.Map("map", {
    center: new naver.maps.LatLng(37.3595316, 127.1052133),//LatLng : 위도 경도를 다룸
    zoom: 18, //배율 지정
    zoomControl: true, //배율을 사용자가 직접 지정할 수 있도록 바를 띄워줌
    zoomControlOptions: { //배율 지정 바 스타일 변경
        position: naver.maps.Position.TOP_RIGHT,
        style: naver.maps.ZoomControlStyle.SMALL
    }
});
map.setCursor('pointer');
var infoWindow = new naver.maps.InfoWindow({
    borderWidth: 0,
    backgroundColor: 'transparent',
    anchorColor: 'var(--point1)',
    anchorSkew: true
});

function searchAddressToCoordinate(address) {
    naver.maps.Service.geocode({
        query: address
    }, function (status, response) {
        if (status === naver.maps.Service.Status.ERROR) {
            return alert('Something Wrong!');
        }

        if (response.v2.meta.totalCount === 0) {
            return alert('totalCount' + response.v2.meta.totalCount);
        }

        var htmlAddresses = [],
            item = response.v2.addresses[0],
            point = new naver.maps.Point(item.x, item.y);// item.x : 경도 item.y : 위도
        $("#mapX").val(item.y);
        $("#mapY").val(item.x);
        if (item.roadAddress) {
            $("#restrAddr1").val(item.roadAddress);
            address = item.roadAddress;
            htmlAddresses.push('<span style="color : var(--pointFont); font-family : g_b;">[도로명 주소] </span>' + item.roadAddress);
        }

        if (item.jibunAddress) {
            htmlAddresses.push('<span style="color : var(--pointFont);  font-family : g_b;">[지번 주소] </span>' + item.jibunAddress);
        }

        infoWindow.setContent([
            '<div class="round-box-child" style="padding:10px;min-width:100px;line-height:150%;">',
            '<h4 style="margin-top:5px;font-size: 15px; font-family : g_b; color : var(--point1);">검색 주소 : ' + address + '</h4><br /><p style="font-size:13px; font-family : g_r">',
            htmlAddresses.join('<br />'),
            '</p></div>'
        ].join('\n'));

        map.setCenter(point);
        infoWindow.open(map, point);
    });
}
//클릭하면 좌표값+주소값 가져오기
function searchCoordinateToAddress(latlng) {

    infoWindow.close();

    naver.maps.Service.reverseGeocode({
        coords: latlng,
        orders: [
            naver.maps.Service.OrderType.ADDR,
            naver.maps.Service.OrderType.ROAD_ADDR
        ].join(',')
    }, function (status, response) {
        if (status === naver.maps.Service.Status.ERROR) {
            return alert('시스템 오류입니다!');
            //sw 쓸 수 있으면 써도 되고..
        }
        var items = response.v2.results,
            address = '',
            htmlAddresses = [];
        item = items[0];
        address = makeAddress(item) || '';
        $("#restrAddr1").val(address);
        $("#restrAddr2").focus();
        searchAddressToCoordinate(address);
    });
}
//클릭 이벤트
map.addListener('click', function (e) {
    searchCoordinateToAddress(e.coord);
});
//주소만들기
function makeAddress(item) {
    if (!item) {
        return;
    }

    var name = item.name,
        region = item.region,
        land = item.land,
        isRoadAddress = name === 'roadaddr';

    var sido = '', sigugun = '', dongmyun = '', ri = '', rest = '';

    if (hasArea(region.area1)) {
        sido = region.area1.name;
    }

    if (hasArea(region.area2)) {
        sigugun = region.area2.name;
    }

    if (hasArea(region.area3)) {
        dongmyun = region.area3.name;
    }

    if (hasArea(region.area4)) {
        ri = region.area4.name;
    }

    if (land) {
        if (hasData(land.number1)) {
            if (hasData(land.type) && land.type === '2') {
                rest += '산';
            }

            rest += land.number1;

            if (hasData(land.number2)) {
                rest += ('-' + land.number2);
            }
        }

        if (isRoadAddress === true) {
            if (checkLastString(dongmyun, '면')) {
                ri = land.name;
            } else {
                dongmyun = land.name;
                ri = '';
            }

            if (hasAddition(land.addition0)) {
                rest += ' ' + land.addition0.value;
            }
        }
    }

    return [sido, sigugun, dongmyun, ri, rest].join(' ');
}

function hasArea(area) {
    return !!(area && area.name && area.name !== '');
}

function hasData(data) {
    return !!(data && data !== '');
}

function checkLastString(word, lastString) {
    return new RegExp(lastString + '$').test(word);
}

function hasAddition(addition) {
    return !!(addition && addition.value);
}
$("#submit").on("click", function () {
    //console.log($("form#restrFrm"));
    //시간 되면 중복체크도 신경써주기
    //메뉴 가격 숫자 아니면 에러표시 해주기
    const menuNames = $("table.table").find("td.menuName");
    const menuPrices = $("table.table").find("span.menuPrice");
    const tags = $(".tag-name");
    if ($("input#restrName").val() == "") {
        swal.fire({
            title: "등록 불가",
            text: "업체명을 입력하지 않으셨습니다",
            icon: "error"
        });
        return;
    }
    if ($("input#restrAddr1").val() == "" || $("input#restrAddr2").val() == "") {
        swal.fire({
            title: "등록 불가",
            text: "주소를 입력하지 않으셨습니다",
            icon: "error"
        });
        return;
    }
    if ($("input#restrTel").val() == "") {
        swal.fire({
            title: "등록 불가",
            text: "전화번호를 입력하지 않으셨습니다",
            icon: "error"
        });
        return;
    }

    if ($("textarea#restrContent").val() == "") {
        swal.fire({
            title: "등록 불가",
            text: "설명을 입력하지 않으셨습니다",
            icon: "error"
        });
        return;
    }
    if (menuNames.length == 0) {
        swal.fire({
            title: "등록 불가",
            text: "메뉴를 입력하지 않으셨습니다",
            icon: "error"
        });
        return;
    }
    menuNames.each(function (index, item) {
        const input = $("<input>");
        input.attr("name", "menuName");
        input.attr("type", "hidden");
        input.val($(item).text());
        $("form#restrFrm").append(input);
    });
    if (menuPrices.length == 0) {
        swal.fire({
            title: "등록 불가",
            text: "메뉴를 입력하지 않으셨습니다",
            icon: "error"
        });
        return;
    }
    menuPrices.each(function (index, item) {
        const input = $("<input>");
        input.attr("name", "menuPrice");
        input.attr("type", "hidden");
        input.val($(item).text());
        $("form#restrFrm").append(input);
    });
    if (tags.length == 0) {
        swal.fire({
            title: "등록 불가",
            text: "태그를 입력하지 않으셨습니다",
            icon: "error"
        });
        return;
    }
    tags.each(function (index, item) {
        const input = $("<input>");
        input.attr("name", "tagName");
        input.attr("type", "hidden");
        input.val($(item).text());
        $("form#restrFrm").append(input);
    });
    const thumnail = $("input#imageFile1").prev().prev().children("img");
    if (thumnail.attr("src") == "") {
        swal.fire({
            title: "등록 불가",
            text: "썸네일 사진을 입력하지 않으셨습니다",
            icon: "error"
        });
        return;
    }
    const image = $("input#imageFile2").prev().prev().children("img");
    if (image.attr("src") == "") {
        swal.fire({
            title: "등록 불가",
            text: "사진 하나를 입력하지 않으셨습니다",
            icon: "error"
        });
        return;
    }
    //$("#submit-input").click();
});

const swal = Swal.mixin({
    customClass: {
        confirmButton: "btn btn-point-swal btn-lg",
        cancelButton: "btn btn-mono-swal btn-lg swal",
        title: "point",
    },
    buttonsStyling: false
});
