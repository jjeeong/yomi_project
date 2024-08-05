function closeModal(obj) {
  const modalBg = $(obj).parents(".modal-bg");
  modalBg.removeClass("open");
}
function openModal(modal) {
  console.log(modal);
  $(modal).addClass("open");
}
function alertFunc(icon){
	swal({
        title: '제목',
        text: '내용내용내용',
        icon: icon
      });
      //이후 페이지 이동 시 추가
      /*
      .then(function(){
    	 window.location = "${loc}"; 
      });
      */
}