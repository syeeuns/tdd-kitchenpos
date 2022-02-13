# 키친포스

## 요구 사항
### Product
- POST : `Product`를 생성함.
  -[ ] Body : `Product`
  -[ ] Body안에 `name`은 반드시 들어가야한다.
  -[ ] Body안에 `name`에는 욕설(profanity)이 들어가면 안된다.
  -[ ] Body안에 `price`는 반드시 들어가야한다.
  -[ ] Body안에 `price`는 0 이상이어야 한다.
    
- PUT : Product의 가격을 변경한다.
  -[ ] PathParam : `productId`
  -[ ] `productId` 는 반드시 이미 등록되어있는 `productId`이어야 한다.
  -[ ] Body : `{price : 가격값}`
  -[ ] Body안에 `price`는 반드시 존재해야한다.
  -[ ] Body안에 `price`는 0 이상이어야 한다.
    
 - GET : `Product` 목록 전체를 조회한다.
    

### Menu (`/api/menus`)
- POST : `Menu`를 생성함.
    -[ ] Body : `Menu`의 List
    -[ ] `menuGroupId`는 반드시 이미 등록되어있는 `menuGroupId`이어야 한다.
    -[ ] Body안에 `MenuProduct`의 List는 반드시 있어야한다.
    -[ ] Body안에 `price`는 반드시 들어가야한다.
    -[ ] Body안에 `price`는 0 이상이어야 한다.
    -[ ] Body안에 `menuProduct`의 `product`는 이미 DB에 저장되어있는 `product`이어야 한다. 
    -[ ] `menuProduct`의 `quantity`는 0이상이어야 한다.
    -[ ] `menu`의 `price`는 `menuProduct`의 `price`*`quantity` 이하이어야 한다.
    -[ ] Body안에 `name`은 반드시 들어가야한다.
    -[ ] Body안에 `name`에는 욕설(profanity)이 들어가면 안된다.
    
- PUT(`/{menuId}/price`) : `menu`의 가격을 변경한다. 
    -[ ] PathParam : `menuId`
    -[ ] `menuId` 는 반드시 이미 등록되어있는 `menuId`이어야 한다.
    -[ ] Body : `{price : 가격값}`
    -[ ] `price`는 반드시 존재해야한다.
    -[ ] `price`는 0 이상이어야 한다.
    -[ ] `menu`의 `price`는 `menuProduct`의 `price`*`quantity` 이하이어야 한다.

- PUT(`/{menuId}/display`) : `displayed`의 상태를 `true`로 변경한다. 
    -[ ] PathParam : `menuId`
    -[ ] `menuId` 는 반드시 이미 등록되어있는 `menuId`이어야 한다.
    -[ ] `menu`의 `price`는 `menuProduct`의 `price`*`quantity` 이하이어야 한다.

- PUT(`/{menuId}/hide`) : `displayed`의 상태를 `false`로 변경한다.
    -[ ] PathParam : `menuId`
    -[ ] `menuId` 는 반드시 이미 등록되어있는 `menuId`이어야 한다.

- GET : `Menu` 목록 전체를 조회한다.


### Menu-groups (`/api/menu-groups`)
- POST : `Menu-groups`를 생성함.
    -[ ] Body : `Menu-group`
    -[ ] Body 안에 `name`은 반드시 들어가야한다.

- GET : `Menu-group` 목록 전체를 조회한다.

### Order (`/api/orders`)
- POST : `Order`를 생성함.
    -[ ] Body : `Order`
    -[ ] Body 안에 `type`은 반드시 있어야한다.
    -[ ] `type`은 `DELIVERY`, `TAKEOUT`, `EAT_IN` 중 하나이어야한다.
    -[ ] Body 안에 `orderLineItem`의 List는 반드시 있어야한다.
    -[ ] Body 안에 `orderLineItem`안의 `menuId`는 이미 DB에 저장되어 있는 `Menu`의 id 이어야 한다.
    -[ ] Body 안에 `menuProduct`의 `product`는 이미 DB에 저장되어있는 `product`이어야 한다.
    -[ ] `type`이 `EAT_IN`인 경우, `orderLineItem`의 `quantity`는 0 이상이어야 한다.
    -[ ] `oderLineItem`의 `menuId`에 해당하는 `Menu`는 `displayed`가 `true`여야한다. 
    -[ ] `oderLineItem`의 `price`는 `menuId`에 해당하는 `Menu`의 `price`와 같아야한다.
    -[ ] `type`이 `DELIVERY`인 경우, Body 안에 `deliveryAddress`가 있어야한다.
    -[ ] `type`이 `EAT_IN`인 경우, Body 안의 `orderTableId`가 이미 DB에 저장되어 있는 `OrderTable`의 id 이어야 한다.
    -[ ] `type`이 `EAT_IN`인 경우, Body 안의 `orderTableId`에 해당하는 `OrderTable`은 `empty`가 `true`이면 안된다.

- PUT(`/{orderId}/accept`) : `orderId`에 해당하는 `Order`의 `status`를 `ACCEPTED`로 변경한다.
    -[ ] PathParam : `orderId`
    -[ ] `orderId`에 해당하는 `Order`는 반드시 이미 등록되어있는 `Order`이어야 한다.
    -[ ] `orderId`에 해당하는 `Order`의 `status`는 `WAITING`이어야 한다.

- PUT(`/{orderId}/servce`) : `orderId`에 해당하는 `Order`의 `status`를 `SERVED`로 변경한다. 
    -[ ] PathParam : `orderId`
    -[ ] `orderId`에 해당하는 `Order`는 반드시 이미 등록되어있는 `Order`이어야 한다.
    -[ ] `orderId`에 해당하는 `Order`의 `status`는 `ACCEPTED`이어야 한다.

- PUT(`/{orderId}/start-delivery`) : `orderId`에 해당하는 `Order`의 `status`를 `DELIVERING`로 변경한다.
    -[ ] PathParam : `orderId`
    -[ ] `orderId`에 해당하는 `Order`는 반드시 이미 등록되어있는 `Order`이어야 한다.
    -[ ] `orderId`에 해당하는 `Order`의 `type`는 `DELIVERY`이어야 한다.
    -[ ] `orderId`에 해당하는 `Order`의 `status`는 `SERVED`이어야 한다.

- PUT(`/{orderId}/complete-delivery`) : `orderId`에 해당하는 `Order`의 `status`를 `DELIVERED`로 변경한다.
    -[ ] PathParam : `orderId`
    -[ ] `orderId`에 해당하는 `Order`는 반드시 이미 등록되어있는 `Order`이어야 한다.
    -[ ] `orderId`에 해당하는 `Order`의 `type`는 `DELIVERY`이어야 한다.
    -[ ] `orderId`에 해당하는 `Order`의 `status`는 `DELIVERING`이어야 한다.

- PUT(`/{orderId}/complete`) : `orderId`에 해당하는 `Order`의 `status`를 `DELIVERED`로 변경한다.
    -[ ] PathParam : `orderId`
    -[ ] `orderId`에 해당하는 `Order`는 반드시 이미 등록되어있는 `Order`이어야 한다.
    -[ ] `orderId`에 해당하는 `Order`의 `type`는 `DELIVERY`일 때는, `status`가 `DELIVERED`이어야 한다.
    -[ ] `orderId`에 해당하는 `Order`의 `type`는 `DELIVERY`가 아니면, `status`가 `SERVED`이어야 한다.

- GET : `Order` 목록 전체를 조회한다.

### Order-table (`/api/order-tables`)
- POST : `Order-table`를 생성함.
    -[ ] Body : `Order-table`
    -[ ] Body 안에 `name`은 반드시 들어가야한다.

- PUT(`/{orderTableId}/sit`) : `Order-table`의 `empty`를 `false`로 한다.
    -[ ] `orderTableId`에 해당하는 `Order-table`는 반드시 이미 등록되어있는 `Order-table`이어야 한다.

- PUT(`/{orderTableId}/clear`) : `Order-table`의 `empty`를 `true`로 하고, `numberOfGuest`를 0으로 한다.
    -[ ] `orderTableId`에 해당하는 `Order-table`는 반드시 이미 등록되어있는 `Order-table`이어야 한다.
    -[ ] `orderTableId`에 해당하는 `Order-table`의 `status`는 `COMPLETED`이어야 한다.

- PUT(`/{orderTableId}/number-of-guests`) : `Order-table`의 `numberOfGuest`를 변경한다.
    -[ ] `orderTableId`에 해당하는 `Order-table`는 반드시 이미 등록되어있는 `Order-table`이어야 한다.
    -[ ] `orderTableId`에 해당하는 `Order-table`의 `empty`는 `false`이어야 한다.
    -[ ] Body : `Order-table`
    -[ ] Body 안의 `Order-talbe`의 `numberOfGuests`는 0 이상이어야 한다.
  
- GET : `Order-table` 목록 전체를 조회한다.

## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
|  |  |  |

## 모델링
