//swal 설정

const swal = Swal.mixin({
    customClass: {
        confirmButton: "btn btn-point-swal btn-lg",
        cancelButton: "btn btn-mono-swal btn-lg swal",
        title: "point",
    },
    confirmButtonText: "확인",
    buttonsStyling: false
});

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
//사진 썸네일2(수정페이지에서 처음 기존거 바꿀 시에..)
function loadImg(obj, filepath, no) {
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
    const delFileInput = $("<input>");
    delFileInput.attr("name", "delFilepath" + no);
    delFileInput.attr("type", "hidden");
    delMenuInput.val(filepath);
    $("form#updateFrm").append(delFileInput);
}

// //메뉴 삭제 버튼
// const delBtn = $("table").find("div.btn-box").children(".btn-mono");
// delBtn.on("click", function () { deleteTr(this) });
// //메뉴 추가 버튼
// const completeBtn = $("table").find("div.btn-box").children(".btn-sub");
// completeBtn.on('click', function () {
//     completeTr(this);
// })

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
//메뉴 추가버튼은 작성페이지와 업데이트 페이지가 다르므로 각각의 페이지에 넣었음


function updateTr(btn) {
    const tr = $(btn).parents("tr");
    const MenuNameTd = $("<td>");
    const MenuNameDiv = $("<div>");
    MenuNameDiv.addClass("input-box");
    const MenuNameInput = $("<input type='text' placeholder='메뉴 이름'>");
    MenuNameInput.addClass("input-td").addClass("round-box").val(tr.children().eq(0).text());
    MenuNameDiv.append(MenuNameInput);
    MenuNameTd.append(MenuNameDiv);
    // 가격
    const MenuPriceTd = $("<td>");
    const MenuPriceDiv = $("<div>");
    MenuPriceDiv.addClass("input-box");
    const MenuPriceInput = $("<input type='text' placeholder='가격'>");
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

//수정페이지
function updateTr(btn, restrMenuNo) {
    const tr = $(btn).parents("tr");
    const MenuNameTd = $("<td>");
    const MenuNameDiv = $("<div>");
    MenuNameDiv.addClass("input-box");
    const MenuNameInput = $("<input type='text' placeholder='메뉴 이름'>");
    MenuNameInput.addClass("input-td").addClass("round-box").val(tr.children().eq(0).text());
    MenuNameDiv.append(MenuNameInput);
    MenuNameTd.append(MenuNameDiv);
    // 가격
    const MenuPriceTd = $("<td>");
    const MenuPriceDiv = $("<div>");
    MenuPriceDiv.addClass("input-box");
    const MenuPriceInput = $("<input type='text' placeholder='가격'>");
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
        deleteTr(this);
    });
    btnDiv.append(insertBtn).append($("<br>")).append(deleteBtn);
    btnTd.append(btnDiv);
    // 합치기
    tr.empty();
    tr.append(MenuNameTd).append(MenuPriceTd).append(btnTd);
    //메뉴 삭제 하기 위한 메뉴 번호 저장하기
    insertDelMenuInput(restrMenuNo);
}
function insertDelMenuInput(restrMenuNo) {
    const delMenuInput = $("<input>");
    delMenuInput.attr("name", "delMenuNo");
    delMenuInput.attr("type", "hidden");
    delMenuInput.val(restrMenuNo);
    $("form#updateFrm").append(delMenuInput);
}

//태그 삭제
// const cancels = $("div.tag").children("span.material-symbols-outlined");

// cancels.on("click", function () {
//     deleteTag(this);
// })

function deleteTag(span) {
    const delTag = $(span).parent();
    delTag.remove();
    //빼는 작업 따로 필요
}

//수정페이지용-수정페이지에서 바뀌는 것들은 모두 class에 update가 추가되어있음/ 기존거 삭제하면 해당 태그, 메뉴번호 저장
function deleteTag(span, restrTagNo) {
    const delTag = $(span).parent();
    delTag.remove();
    //빼는 작업 따로 필요
    const delTagInput = $("<input>");
    delTagInput.attr("name", "delTagNo");
    delTagInput.attr("type", "hidden");
    delTagInput.val(restrTagNo);
    $("form#updateFrm").append(delTagInput);
}




