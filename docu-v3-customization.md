# **Personalizar Revisa y Confirma**

Si deseas agregar una fila customizada en nuestra pantalla de Revisa y Confirma puedes hacerlo siguiendo los siguientes pasos:


## 1. Crea tu vista customizada
 
[Android]
 
Crea un layout con la vista deseada, que esté contenido en un FrameLayout con layout_height=”wrap_content” y layout_width=”match_parent”. Además deberá tener una línea separadora abajo de todo, para diferenciarlo de otras filas. Se recomienda que el layout siga el estilo de las demás filas del revisa y confirma de la SDK.
Prueba con el ejemplo a continuación de un integrador que permite recargar celulares y desea agregar una fila con el número:

 **Excepto el LinearLayout, el resto debería respetarse para que la vista guarde coherencia con las demás filas de la pantalla de Revisa y Confirma. **

```
<?xml version="1.0" encoding="utf-8"?>

<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@color/mpsdk_review_gray_background">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="center_vertical"
        android:gravity="center_vertical"
        android:orientation="vertical"
        android:paddingBottom="20dp"
        android:paddingTop="20dp">

        <ImageView
            android:id="@+id/phoneImage"
            android:layout_width="48dp"
            android:layout_height="48dp"
            android:layout_gravity="center_horizontal"
            android:src="@android:drawable/stat_sys_phone_call"
            android:tint="@color/mpsdk_background_blue" />

        <com.mercadopago.customviews.MPTextView
            android:id="@+id/titleText"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="20dp"
            android:gravity="center_horizontal"
            android:paddingEnd="25dp"
            android:paddingLeft="25dp"
            android:paddingRight="25dp"
            android:paddingStart="25dp"
            android:text="Número de teléfono:"
            android:textColor="@color/mpsdk_review_payment_text"
            android:textSize="24dp"/>

        <com.mercadopago.customviews.MPTextView
            android:id="@+id/phoneNumber"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:includeFontPadding="false"
            android:textSize="28dp"
            app:fontStyle="light"
            tools:text="1522333333" />

    </LinearLayout>

    <View
        android:id="@+id/mpsdkSeparator"
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:layout_gravity="bottom"
        android:background="@color/mpsdk_separator" />
</FrameLayout>

```
 
Luego crea una clase que representará la vista customizada. Será una clase que extienda de Reviewable, y deberá implementar los métodos inflateInParent, initializeControls, getView y draw. 

 - **inflateInParent:** Recibe el ViewGroup padre de la sdk en el cual se inflará la vista customizada. En este método se indica el layout de la vista customizada.
 - **initializeControls:** Se deben inicializar los elementos de la vista que se quieran modificar dinámicamente.
 - **getView:**  Devuelve una instancia de la View que se infló en inflateInParent.
 - **draw:** Se setean los valores a los elementos de la vista, así como también listeners que se deseen tener.

En el constructor de esta clase se podrán pasar todos los parámetros necesarios para dibujar la vista.
Prueba el siguiente ejemplo: 

```
public class CellphoneReview extends Reviewable {

    protected View mView;
    protected TextView mNumberTextView;

    private Context mContext;
    private String mNumber;

    public CellphoneReview(Context context, String cellphoneNumber) {
        this.mContext = context;
        this.mNumber = cellphoneNumber;
    }

    @Override
    public View getView() {
        return mView;
    }

    @Override
    public View inflateInParent(ViewGroup parent, boolean attachToRoot) {
    //Aquí referencia a tu vista customizada
        mView = LayoutInflater.from(mContext)
                .inflate(R.layout.cellphone_review, parent, attachToRoot);
        return mView;
    }

    @Override
    public void initializeControls() {
        mNumberTextView = (TextView) mView.findViewById(R.id.phoneNumber);
    }

    @Override
    public void draw() {
        mNumberTextView.setText(mNumber);
    }
}

```

 
[Objective-C]
 
Crea una celda que extienda de UITableViewCell (también crea el xib). Dicha celda deberá implementar la interfaz MPCellContentProvider, cuyo único método (getHeight()) debería devolver la altura que necesitamos reservar para tu celda.

Podes copiar el siguiente ejemplo, que representa una celda con dos labels (titleLabel y subtitleLabel): 

**Recordá linkear los labels desde el xib a los IBOutlets** 
 
```
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
 
@import MercadoPagoSDK;
 
@interface CellphoneTableViewCell : UITableViewCell<MPCellContentProvider>
 
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UILabel *subtitleLabel;
 
@end
 
``` 

```
 
 #import "CellphoneTableViewCell.h"
@import MercadoPagoSDK;
 
@implementation CellphoneTableViewCell
 
 
-(CGFloat)getHeight {
    return (CGFloat)180;
}
 
@end

```
 
[Swift]

Crea una celda que extienda de UITableViewCell (también crea el xib). Dicha celda deberá implementar la interfaz MPCellContentProvider, cuyo único método (getHeight()) debería devolver la altura que necesitamos reservar para tu celda.

Podes copiar el siguiente ejemplo, que representa una celda con dos labels (titleLabel y subtitleLabel): 

**Recordá linkear los labels desde el xib a los IBOutlets** 

```
import UIKit
import MercadoPagoSDK

class CellphoneTableViewCell: UITableViewCell, MPCellContentProvider {

    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var subtitleLabel: UILabel!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func getHeight() -> CGFloat{
        return 150.0
    }
}
```

 
## 2. Crea una ReviewScreenPreference 


[Android]
 
Para iniciar el Checkout con la pantalla de Revisa y Confirma personalizada debes crear una ReviewScreenPreference agregándole tu [implementación de Reviewable](#1.-crea-tu-vista-customizada).

Pruébalo con el siguiente ejemplo:

```        
        ReviewScreenPreference reviewScreenPreference = new ReviewScreenPreference.Builder()
                .addReviewable(new CellphoneReview(this, "1522333333"))
                .build();

```


[Objective-C]
 
Para agregar una celda a la pantalla de Revisa y Confirma debes crear una ReviewScreenPreference agregándole tu [implementación de MPCellContentProvider](#1.-crea-tu-vista-customizada) a través de una MPCustomCell.

```
 
CellphoneTableViewCell *cellphoneCell = [[[NSBundle mainBundle] loadNibNamed:@"CellphoneTableViewCell" owner:self options:nil] firstObject];
    cellphoneCell.titlelabel.text = @"Número de teléfono:";
    cellphoneCell.subtitleLabel.text = @"1522333333";
 
    MPCustomCell *cellphoneReview = [[MPCustomCell alloc] initWithCell:cellphoneCell];
 
ReviewScreenPreference *reviewScreenPreference = [[ReviewScreenPreference alloc] init];
 [reviewScreenPreference setAddionalInfoCellsWithCustomCells:[NSArray arrayWithObjects:cellphoneReview, nil]];
 
```

[Swift]

Para agregar una celda a la pantalla de Revisa y Confirma debes crear una ReviewScreenPreference agregándole tu [implementación de MPCellContentProvider](#1.-crea-tu-vista-customizada) a través de una MPCustomCell.

```
let cellphoneCell = Bundle.main.loadNibNamed("CellphoneTableViewCell", owner: self, options: nil)?.first as! CellphoneTableViewCell
cellphoneCell.titleLabel?.text = "Número de teléfono"
cellphoneCell.subtitleLabel?.text = "1522333333"

let mpCellphoneCell = MPCustomCell(cell: cellphoneCell)

var reviewScreenPreference = ReviewScreenPreference()
reviewScreenPreference.setAddionalInfoCells(customCells: [mpCellphoneCell])
```
 
### Setear más de una vista Custom

[Android]

Puedes setear más de una vista custom en Revisa y Confirma simplemente agregando las implementaciones de Reviewable a la ReviewScreenPreference antes de iniciar el Checkout con dicha preferencia. 

Prueba con el siguiente ejemplo:

```
        ReviewScreenPreference reviewScreenPreference = new ReviewScreenPreference.Builder()
        //Primera vista custom
                .addReviewable(new CellphoneReview(this, "1522333333"))
        //Segunda vista custom
                .addReviewable(new FunderCustomView(this, "Auspiciante"))
                .setReviewOrder(order)
                .build();
```


[Objective-C]
   
Puedes setear más de una vista custom en Revisa y Confirma simplemente agregando las implementaciones de [MPCellContentProvider](#1.-crea-tu-vista-customizada) que necesites a la ReviewScreenPreference a través de un MPCustomCell antes de iniciar el Checkout con dicha preferencia. 

Prueba con el siguiente ejemplo:

```
CellphoneTableViewCell *cellphoneCell = [[[NSBundle mainBundle] loadNibNamed:@"CellphoneTableViewCell" owner:self options:nil] firstObject];
    MPCustomCell *cellphoneReview = [[MPCustomCell alloc] initWithCell:cellphoneCell];
 
FunderTableViewCell *funderCell = [[[NSBundle mainBundle] loadNibNamed:@"FunderTableViewCell" owner:self options:nil] firstObject];
    MPCustomCell *funderReview = [[MPCustomCell alloc] initWithCell:funderCell];
 
ReviewScreenPreference *reviewScreenPreference = [[ReviewScreenPreference alloc] init];
 [reviewScreenPreference setAddionalInfoCellsWithCustomCells:[NSArray arrayWithObjects:cellphoneReview, funderReview,nil]];
```

[Swift]

Puedes setear más de una vista custom en Revisa y Confirma simplemente agregando las implementaciones de [MPCellContentProvider](#1.-crea-tu-vista-customizada) que necesites a la ReviewScreenPreference a través de un MPCustomCell antes de iniciar el Checkout con dicha preferencia. 

```
let cellphoneCell = Bundle.main.loadNibNamed("CellphoneTableViewCell", owner: self, options: nil)?.first as! CellphoneTableViewCell
cellphoneCell.titleLabel?.text = "Número de teléfono"
cellphoneCell.subtitleLabel?.text = "1522333333"
let mpCellphoneCell = MPCustomCell(cell: cellphoneCell)

let funderCell = Bundle.main.loadNibNamed("FunderTableViewCell", owner: self, options: nil)?.first as! FunderTableViewCell
funderCell.funderLabel?.text = "Auspiciante"
let mpFunderCell = MPCustomCell(cell: funderCell)
        
var reviewScreenPreference = ReviewScreenPreference()
reviewScreenPreference.setAddionalInfoCells(customCells: [mpCellphoneCell,mpFunderCell])
```

 
## 3. Inicia el Checkout con la Preferencia
 
Inicia el Checkout agregando la preferencia [ReviewScreenPreference creada](#2.-crea-una-reviewscreenpreference).
Es este caso, presentamos un ejemplo con el tipo de integración que requiere [tener la preferencia de checkout en nuestros servidores](#REFERENCIA AL DOCU DE MATI):

[Android]
 
```
new MercadoPagoCheckout.Builder()
	.setActivity(this)
.setId(mCheckoutPreferenceId)
.setReviewScreenPreference(reviewScreenPreference)
	.setPublicKey(mPublicKey)
	.startForPayment();
 
```
 
[Objective-C]
 
```
MercadoPagoCheckout *mpCheckout = [[MercadoPagoCheckout alloc] initWithPublicKey: TEST_PUBLIC_KEY accessToken: nil checkoutPreference:self.pref paymentData:nil discount:nil navigationController:self.navigationController paymentResult: nil];
    // Creas la review screen preference
	[mpCheckout setReviewScreenPreference:reviewScreenPreference];
	[mpCheckout start];
```
 
 [Swift]

```
let checkout = MercadoPagoCheckout.init(publicKey: self.publicKey, accessToken: self.accessToken, checkoutPreference: pref, paymentData: paymentData, navigationController: self.navigationController!, paymentResult: paymentResult)
checkout.setReviewScreenPreference(reviewScreenPreference)

``` 

## 4. Agrega la posibilidad de que el usuario cambie su elección:

Si quieres que el usuario pueda realizar un cambio en su elección sobre la vista customizada, debes seguir los siguientes pasos para completar el pago:

### a. Agrega la opción a la vista

[Android]
 
Agrega un Button o TextView a la vista customizada que permita al usuario modificar sus datos. 
Aquí agregamos un TextView al [ejemplo de Layout anterior](#1.-crea-un-layout-con-tu-vista-customizada:) para que puedas probarlo:

```
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@color/mpsdk_review_gray_background">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="center_vertical"
        android:gravity="center_vertical"
        android:orientation="vertical"
        android:paddingBottom="20dp"
        android:paddingTop="20dp">

        <ImageView
            android:id="@+id/phoneImage"
            android:layout_width="48dp"
            android:layout_height="48dp"
            android:layout_gravity="center_horizontal"
            android:src="@android:drawable/stat_sys_phone_call"
            android:tint="@color/mpsdk_background_blue" />

        <com.mercadopago.customviews.MPTextView
            android:id="@+id/titleText"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="20dp"
            android:gravity="center_horizontal"
            android:paddingEnd="25dp"
            android:paddingLeft="25dp"
            android:paddingRight="25dp"
            android:paddingStart="25dp"
            android:text="Número de teléfono:"
            android:textColor="@color/mpsdk_review_payment_text"
            android:textSize="24dp"/>
        
        <com.mercadopago.customviews.MPTextView
            android:id="@+id/phoneNumber"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:includeFontPadding="false"
            android:textSize="28dp"
            app:fontStyle="light"
            tools:text="1522333333" />

        <com.mercadopago.customviews.MPTextView
            android:id="@+id/phoneNumberEdition"
            android:layout_width="match_parent"
            android:layout_height="45dp"
            android:layout_marginEnd="25dp"
            android:layout_marginLeft="25dp"
            android:layout_marginRight="25dp"
            android:layout_marginStart="25dp"
            android:layout_marginTop="20dp"
            android:gravity="center"
            android:background="@color/mpsdk_review_gray_background"
            android:textColor="@color/mpsdk_background_blue"
            android:textAllCaps="false"
            android:text="Cambiar"
            android:textSize="18dp" />

    </LinearLayout>

    <View
        android:id="@+id/mpsdkSeparator"
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:layout_gravity="bottom"
        android:background="@color/mpsdk_separator" />
</FrameLayout>
```
 
[Objective-C]
 
Agrega un botón a la vista customizada que permita al usuario modificar sus datos.

Prueba con el siguiente ejemplo:
 
```
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
 
@import MercadoPagoSDK;
 
@interface CellphoneTableViewCell : UITableViewCell<MPCellContentProvider>
 
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UILabel *subtitleLabel;
@property (weak, nonatomic) IBOutlet UIButton *button;
 
 
@end
 
```
 Setea las propiedades del botón: 
 
```
CellphoneTableViewCell *cellphoneCell = [[[NSBundle mainBundle] loadNibNamed:@"CellphoneTableViewCell" owner:self options:nil] firstObject];
    cellphoneCell.titlelabel.text = @"Número de teléfono:";
    cellphoneCell.subtitlelabel.text = @"1522333333";
 [cellphoneCell.button setTitle:@"Cambiar" forState:UIControlStateNormal];
 
```

[Swift]
 
 Agrega un botón a la vista customizada que permita al usuario modificar sus datos.
Prueba con el siguiente ejemplo:
 
```
import UIKit
import MercadoPagoSDK

class CellphoneTableViewCell: UITableViewCell, MPCellContentProvider {

    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var subtitleLabel: UILabel!
    @IBOutlet weak var button: UIButton!
   
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func getHeight() -> CGFloat{
        return 150.0
    }
}
```

Setea las propiedades del botón: 

```
let cellphoneCell = Bundle.main.loadNibNamed("CellphoneTableViewCell", owner: self, options: nil)?.first as! CellphoneTableViewCell
cellphoneCell.titleLabel?.text = "Número de teléfono"
cellphoneCell.subtitleLabel?.text = "1522333333"
cellphoneCell.button.text = "Cambiar"
       
```

### b. Responde a los cambios del usuario
 
 
[Android]


Debes configurar un listener de la View creada en el paso anterior y en él llamar al método notifyChangeRequired(REQUEST_CODE) de Reviewable. Tienes que seatearle un REQUEST_CODE para que puedas escuchar este cambio en tu aplicación y actuar en consecuencia.
Puedes copiar el siguiente ejemplo para probarlo, donde el usuario desea recargar otro número telefónico:

```
public class CellphoneReview extends Reviewable {

    public static final Integer CELLPHONE_CHANGE_REQUEST_CODE = 321321;

    protected View mView;
    protected TextView mNumberTextView;
    protected View mNumberEdition;

    private Context mContext;
    private String mNumber;

    public CellphoneReview(Context context, String cellphoneNumber) {
        this.mContext = context;
        this.mNumber = cellphoneNumber;
    }

    @Override
    public View getView() {
        return mView;
    }

    @Override
    public View inflateInParent(ViewGroup parent, boolean attachToRoot) {
        mView = LayoutInflater.from(mContext)
                .inflate(R.layout.cellphone_review, parent, attachToRoot);
        return mView;
    }

    @Override
    public void initializeControls() {
        mNumberTextView = (TextView) mView.findViewById(R.id.phoneNumber);
        mNumberEdition = mView.findViewById(R.id.phoneNumberEdition);

    }

    @Override
    public void draw() {

        mNumberTextView.setText(mNumber);
        mNumberEdition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyChangeRequired(CELLPHONE_CHANGE_REQUEST_CODE);
            }
        });
    }
}


```
 
Luego de escuchar el REQUEST_CODE en tu aplicación y realizar el cambio solicitado por el usuario, puedes reiniciar el checkout con el PaymentData recibido al escuchar el MercadoPagoCheckout.CHECKOUT_REQUEST_CODE. 
Dicho objeto contiene los datos de pago obtenidos hasta el momento, de ésta forma el usuario no tendrá que volver a ingresarlos al realizar modificaciones en la pantalla de Revisa y Confirma. 

Puedes obtener el PaymentData de la siguiente forma:

```
@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LayoutUtil.showRegularLayout(this);
        if (requestCode == MercadoPagoCheckout.CHECKOUT_REQUEST_CODE) {
            if (resultCode == CellphoneReview.CELLPHONE_CHANGE_REQUEST_CODE){
                Toast.makeText(mActivity, "Cambiar número!", Toast.LENGTH_SHORT).show();

                PaymentData paymentData = JsonUtil.getInstance().fromJson(data.getStringExtra("paymentData"), PaymentData.class);
                restartCheckout(paymentData);
            }
        }
    }
```

Y de ésta manera reinicias el Checkout con el mismo:

```

private void restartCheckout(PaymentData paymentData) {

        ReviewScreenPreference reviewScreenPreference = new ReviewScreenPreference.Builder()
                .addReviewable(new CellphoneReview(this, "1522333333"))
                .build();

        new MercadoPagoCheckout.Builder()
                .setActivity(this)
                .setPaymentData(paymentData)
                .setReviewScreenPreference(reviewScreenPreference)
                .setPublicKey(mPublicKey)
                .setCheckoutPreference(getCheckoutPreference())
                .startForPayment();
    }


```

 
[Objective-C]
 
Agrega un target al botón que apunte al comportamiento que esperas.
Prueba con el siguiente ejemplo:
 
```
 CellphoneTableViewCell *cellphoneCell = [[[NSBundle mainBundle] loadNibNamed:@"CellphoneTableViewCell" owner:self options:nil] firstObject];
    cellphoneCell.titleLabel.text = @"Número de teléfono";
    cellphoneCell.subtitleLabel.text = @"1522333333";
    [cellphoneCell.button setTitle:@"Cambiar" forState:UIControlStateNormal];
    [cellphoneCell.button addTarget:self action:@selector(cambiarNumero) forControlEvents:UIControlEventTouchUpInside];
    MPCustomCell *mpCellphoneCell = [[MPCustomCell alloc] initWithCell:cellphoneCell];
 
```
Configura tu comportamiento y luego refresca la pantalla de Revisa y Confirma para observar los cambios:

```
-(void) cambiarNumero {
    //Haz tus cambios, crea nuevas celdas con los cambios actualizados
    
    ReviewScreenPreference* reviewScreenPreference = [[ReviewScreenPreference alloc]init];
    [reviewScreenPreference setAddionalInfoCellsWithCustomCells:[NSArray arrayWithObjects:updatedMpCellphoneCell, updatedMpFunderCell, nil]]
    [self.mpCheckout setReviewScreenPreference:reviewScreenPreference];
    [self.mpCheckout updateReviewAndConfirm];
}
```

[Swift]
 
 Agrega un target al botón que apunte al comportamiento que esperas.
 Prueba con el siguiente ejemplo:
 
```
let cellphoneCell = Bundle.main.loadNibNamed("CellphoneTableViewCell", owner: self, options: nil)?.first as! CellphoneTableViewCell
cellphoneCell.titleLabel?.text = "Número de teléfono"
cellphoneCell.subtitleLabel?.text = "1522333333"
cellphoneCell.button.text = "Cambiar"
cellphoneCell.button.addTarget(self, action: "cambiarNumero", for: .touchUpInside)
        
let mpCellphoneCell = MPCustomCell(cell: cellphoneCell)
 
``` 
Configura tu comportamiento y luego refresca la pantalla de Revisa y Confirma para observar los cambios:

```
func cambiarNumero(){
    //Haz tus cambios, crea nuevas celdas con los cambios actualizados
 
    var reviewScreenPreference = ReviewScreenPreference()
    reviewScreenPreference.setAddionalInfoCells(customCells: [updatedMpCellphoneCell,updatedMpFunderCell])
    self.checkout.setReviewScreenPreference(reviewScreenPreference)
    self.checkout.updateReviewAndConfirm()
}
 
```

# 5. Personaliza los títulos de la pantalla Revisa y Confirma:
 
Puedes cambiar títulos y nombres de botones agregando dichas preferencias al crear la ReviewScreenPreference:
 
[Android]
 
**setTitle:** Reemplaza el título de la pantalla de Revisa y Confirma.
**setCancelText:** Reemplaza el texto del link que cancela el pago.
**setConfirmText:** Reemplaza el texto del botón que confirma el pago.
**setProductDetail:** Reemplaza la descripción del producto a pagar en la lista de items de la pantalla.
 
Prueba con el siguiente ejemplo:
 
```
ReviewScreenPreference reviewScreenPreference = new ReviewScreenPreference.Builder()
               .addReviewable(new CellphoneReview(this, "1522333333"))
               .setTitle("Confirma tu Recarga")
.setCancelText("Cancelar Recarga")
 .setConfirmText("Recargar")
               .setProductDetail("Recarga de Celular")
               .build();
```
[Objective - c]
 
Tienes las siguientes opciones:
 
**setTitleWithTitle:** Reemplaza el título de la pantalla de Revisa y Confirma.
**setCancelButtonTextWithCancelButtonText:** Reemplaza el texto del link que cancela el pago.
**setConfirmButtonTextWithConfirmButtonText:** Reemplaza el texto del botón que confirma el pago.
**setProductsDetailWithProductsTitle:** Reemplaza la descripción del producto a pagar en la lista de items de la pantalla.
 
Prueba con el siguiente ejemplo:
 
```
ReviewScreenPreference *reviewScreenPreference = [[ReviewScreenPreference alloc] init];
    [reviewScreenPreference setTitleWithTitle:@"Confirma tu Recarga"];
 [reviewScreenPreference setCancelButtonTextWithCancelButtonText:@"Cancelar Recarga"];
    [reviewScreenPreference setConfirmButtonTextWithConfirmButtonText:@"Recargar"];
    [reviewScreenPreference setProductsDetailWithProductsTitle:@"Recarga de Celular"];
   
```
 
[Swift]
 
Tienes las siguientes opciones:
 
**setTitle:** Reemplaza el título de la pantalla de Revisa y Confirma.
**setCancelButtonText:** Reemplaza el texto del link que cancela el pago.
**setConfirmButtonText:** Reemplaza el texto del botón que confirma el pago.
**setProductsDetail:** Reemplaza la descripción del producto a pagar en la lista de items de la pantalla.
 
Prueba con el siguiente ejemplo:


```
var reviewScreenPreference = ReviewScreenPreference()

reviewScreenPreference.setTitle(title: "Confirma Tu Recarga")
        reviewScreenPreference.setCancelButtonText(cancelButtonText: "Cancelar Recarga")
        reviewScreenPreference.setConfirmButtonText(confirmButtonText: "Recargar")
        reviewScreenPreference.setProductsDetail(productsTitle: "Recarga de Celular")
```
 
 
 ----------
 
 # **Personalización de pantallas de resultados**

Puedes customizar las pantallas de resultados, agregando filas personalizadas, cambiando títulos, subtítulos y botones a través de la preferencia **PaymentResultScreenPreference**. 
Hazlo en dos simples pasos:

### 1. Crea la preferencia PaymentResultScreenPreference

Debes crear la preferencia agregando los cambios que deseas aplicar a las pantallas de resultados. Puedes [cambiar títulos](#cambiar-títulos-en-las-pantallas-de-resultados) y/o agregar [filas personalizadas](#AGREGAR-LINK) a las mismas.
 
Prueba con el siguiente ejemplo:
 
[Android]
```
PaymentResultScreenPreference paymentResultScreenPreference = new PaymentResultScreenPreference.Builder()
       .setApprovedTitle("¡Listo, recargaste el celular!")
       .build();

```
 
[Objective-C]
```
PaymentResultScreenPreference *resultPreference = [[PaymentResultScreenPreference alloc]init];
[resultPreference setApprovedTitleWithTitle:@"¡Listo, recargaste el celular!"];
```
 
[Swift] 
```
var paymentResultScreenPreference = PaymentResultScreenPreference()
paymentResultScreenPreference.setApprovedTitle(title: "¡Listo, recargaste el celular!")
 
```
 
### 2. Inicia el Checkout con la PaymentResultScreenPreference
 
Inicia el Checkout agregando la preferencia [PaymentResultScreenPreference creada](#2.-crea-una-reviewscreenpreference).
Es este caso, presentamos un ejemplo con el tipo de integración que requiere [tener la preferencia de checkout en nuestros servidores](#REFERENCIA AL DOCU):
 
[Android]
```
new MercadoPagoCheckout.Builder()
	.setActivity(this)
.setId(mCheckoutPreferenceId)
.setPaymentResultScreenPreference(paymentResultScreenPreference)
	.setPublicKey(mPublicKey)
	.startForPayment();
 
```
 
[Objective - C]
 
```
MercadoPagoCheckout *mpCheckout = [[MercadoPagoCheckout alloc] initWithPublicKey: TEST_PUBLIC_KEY accessToken: nil checkoutPreference:self.pref paymentData:nil discount:nil navigationController:self.navigationController paymentResult: nil];
    // Aquí creas la PaymentResultScreenPreference
[mpCheckout setPaymentResultScreenPreference:resultPreference];
	[mpCheckout start];
 
```
 
[Swift]
 
```
var mpCheckout = MercadoPagoCheckout(publicKey: TEST_PUBLIC_KEY, accessToken: nil, checkoutPreference: self.pref, navigationController: self.navigationController)
        mpCheckout.setPaymentResultScreenPreference(paymentResultScreenPreference)
 
```

## **Cambiar títulos en la pantalla de resultados**

### Pantalla de Pago Aprobado

La misma se muestra si el resultado del pago fue aprobado y no se necesita ninguna acción por parte del usuario para finalizarlo, (como acercarse a algún atm, realizar una transferencia desde la web de un banco). Este es el caso de los pagos con tarjetas de crédito o débito. 

#### Crea la preferencia con los cambios

[Android]

Puedes realizar los siguientes cambios sobre la misma:

- **setApprovedTitle:** Para cambiar el título de la pantalla de pago aprobado. Si no se setea, se muestra el título default de la SDK
- **setApprovedSubtitle:** Para cambiar el subtítulo de la pantalla de pago aprobado. Si no se setea, no se muestra nada.


Prueba el siguiente ejemplo:

```
PaymentResultScreenPreference paymentResultScreenPreference = new PaymentResultScreenPreference.Builder()
       .setApprovedTitle("Recargaste tu celular!")
       .setApprovedSubtitle("Número 12234324 - Movistar")
       .build();
```

El ejemplo se ve así: [IMAGEN](https://drive.google.com/open?id=0B6eJRTzx7kk7bVJsV29YZjZnM1U)
 
[Objective-C]
 
Puedes realizar los siguientes cambios sobre la misma:

- **setApprovedTitleWithTitle:** Para cambiar el título de la pantalla de pago aprobado. Si no se setea, se muestra el título default de la SDK.
- **setApprovedSubtitleWithSubtitle:** Para cambiar el subtítulo de la pantalla de pago aprobado. Si no se setea, no se muestra nada.


Prueba el siguiente ejemplo:
 
```
PaymentResultScreenPreference *resultPreference = [[PaymentResultScreenPreference alloc]init];
[resultPreference setApprovedTitleWithTitle:@"¡Listo, recargaste el celular"];
[resultPreference setApprovedSubtitleWithSubtitle:@"Número 12234324 - Movistar"];
 
```
[Swift]
 
Puedes realizar los siguientes cambios sobre la misma:

- **setApprovedTitle:** Para cambiar el título de la pantalla de pago aprobado. Si no se setea, se muestra el título default de la SDK.
- **setApprovedSubtitle:** Para cambiar el subtítulo de la pantalla de pago aprobado. Si no se setea, no se muestra nada.
 
```
   var resultPreference = PaymentResultScreenPreference()
        resultPreference.setApprovedTitle(title: "¡Listo, recargaste el celular!")
        resultPreference.setApprovedSubtitle(subtitle: "Número 12234324 - Movistar")
        
```

## **Agregar fila personalizada a la Pantalla de Resultados**
 
## 1. Crea tu vista customizada

[Android]

Crea un layout con la vista deseada, que esté contenido en un FrameLayout con layout_height=”wrap_content” y layout_width=”match_parent”. Además deberá tener una línea separadora abajo de todo, para diferenciarlo de otras filas. Se recomienda que el layout siga el estilo de las demás filas del revisa y confirma de la SDK.
 
Prueba con el siguiente ejemplo:
 
```
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
   android:layout_width="match_parent"
   android:layout_height="wrap_content"
   android:background="@color/mpsdk_review_gray_background">
 
   <com.mercadopago.customviews.MPTextView
       android:id="@+id/textView"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       android:layout_gravity="center_horizontal"
       android:layout_margin="20dp"
       android:gravity="center_horizontal"
       android:paddingEnd="15dp"
       android:paddingLeft="15dp"
       android:paddingRight="15dp"
       android:paddingStart="15dp"
       android:textColor="@color/mpsdk_review_payment_text"
       android:textSize="18dp" />
 
   <View
       android:id="@+id/mpsdkSeparator"
       android:layout_width="match_parent"
       android:layout_height="1dp"
       android:layout_gravity="bottom"
       android:background="@color/mpsdk_separator" />
 
</FrameLayout>
 
```
 
Luego crea una clase que representará la vista customizada. Será una clase que extienda de Reviewable, y deberá implementar los métodos inflateInParent, initializeControls, getView y draw. 

 - **inflateInParent:** Recibe el ViewGroup padre de la sdk en el cual se inflará la vista customizada. En este método se indica el layout de la vista customizada.
 - **initializeControls:** Se deben inicializar los elementos de la vista que se quieran modificar dinámicamente.
 - **getView:**  Devuelve una instancia de la View que se infló en inflateInParent.
 - **draw:** Se setean los valores a los elementos de la vista, así como también listeners que se deseen tener.

En el constructor de esta clase se podrán pasar todos los parámetros necesarios para dibujar la vista.
Prueba el siguiente ejemplo: 
 
```
public class CongratsReview extends Reviewable {
 
   protected View mView;
   protected TextView mTextView;
 
   private Context mContext;
   private String mText;
 
   public CongratsReview(Context context, String text) {
       this.mContext = context;
       this.mText = text;
   }
 
   @Override
   public View getView() {
       return mView;
   }
 
   @Override
   public View inflateInParent(ViewGroup parent, boolean attachToRoot) {
       mView = LayoutInflater.from(mContext)
               .inflate(R.layout.congrats_review, parent, attachToRoot);
       return mView;
   }
 
   @Override
   public void initializeControls() {
       mTextView = (TextView) mView.findViewById(R.id.textView);
   }
 
   @Override
   public void draw() {
       mTextView.setText(mText);
   }
}
 
```

[Objective-C]

Crea una celda que extienda de UITableViewCell (también crea el xib). Dicha celda deberá implementar la interfaz MPCellContentProvider, cuyo único método (getHeight()) debería devolver la altura que necesitamos reservar para tu celda.

Podes copiar el siguiente ejemplo, que representa una celda con dos labels (titleLabel y subtitleLabel): 

**Recordá linkear los labels desde el xib a los IBOutlets** 
 
```
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
 
@import MercadoPagoSDK;
 
@interface CongratsTableViewCell : UITableViewCell<MPCellContentProvider>
 
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
 
@end
 
``` 

```
 
 #import "CongratsTableViewCell.h"
@import MercadoPagoSDK;
 
@implementation CongratsTableViewCell
 
 
-(CGFloat)getHeight {
    return (CGFloat)180;
}
 
@end

```
 
[Swift]

Crea una celda que extienda de UITableViewCell (también crea el xib). Dicha celda deberá implementar la interfaz MPCellContentProvider, cuyo único método (getHeight()) debería devolver la altura que necesitamos reservar para tu celda.

Podes copiar el siguiente ejemplo, que representa una celda con dos labels (titleLabel y subtitleLabel): 

**Recordá linkear los labels desde el xib a los IBOutlets** 

```
import UIKit
import MercadoPagoSDK

class CongratsTableViewCell: UITableViewCell, MPCellContentProvider {

    @IBOutlet weak var titleLabel: UILabel!
   
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func getHeight() -> CGFloat{
        return 150.0
    }
}
```

## 2. Agrega la vista customizada a la preferencia PaymentResultScreenPreference:
 
[Android]
 
```
CongratsReview congratsReview = new CongratsReview(this, "También programaste esta recarga para el 15 de cada mes");
 
PaymentResultScreenPreference paymentResultScreenPreference = new PaymentResultScreenPreference.Builder()
	.addCongratsReviewable(congratsReview)
.build();
 
```
 
[Objective-C]

Para agregar una fila a la pantalla de Resultados debes crear una ReviewScreenPreference agregándole tu [implementación de MPCellContentProvider](#1.-crea-tu-vista-customizada) a través de una MPCustomCell.

```
 
CongratsTableViewCell *congratsCell = [[[NSBundle mainBundle] loadNibNamed:@"CongratsTableViewCell" owner:self options:nil] firstObject];
congratsCell.titlelabel.text = @"También programaste esta recarga para el 15 de cada mes";

MPCustomCell *congratsCustomCell = [[MPCustomCell alloc] congratsCell];

PaymentResultScreenPreference *resultPreference = [[PaymentResultScreenPreference alloc]init];

[resultPreference setCustomsApprovedCellWithCustomCells:[NSArray arrayWithObjects:congratsCustomCell, nil]];
 
```


[Swift]

Para agregar una fila a la pantalla de Resultados debes crear una ReviewScreenPreference agregándole tu [implementación de MPCellContentProvider](#1.-crea-tu-vista-customizada) a través de una MPCustomCell.


```
let congratsCell = Bundle.main.loadNibNamed("CongratsTableViewCell", owner: self, options: nil)?.first as! CongratsTableViewCell

let congratsCustomCell = MPCustomCell(cell: congratsCell)
var resultPreference = PaymentResultScreenPreference()     
resultPreference.setCustomsApprovedCell(customCells: [congratsCustomCell])
 
```
 

 

