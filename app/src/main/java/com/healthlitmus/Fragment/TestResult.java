package com.healthlitmus.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.healthlitmus.Helper.ContentTestResult;
import com.healthlitmus.Helper.CustomLinearLayoutManager;
import com.healthlitmus.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestResult extends Fragment {


    public TestResult() {
        // Required empty public constructor
    }

    private ContentTestResult contentTestResult;
    private RVAdapter rvAdapter;
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test_result, container, false);

        contentTestResult = new ContentTestResult();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_card_test);
        recyclerView.setHasFixedSize(false);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        CustomLinearLayoutManager linearLayoutManager = new CustomLinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        contentTestResult.clear();
        contentTestResult.addItem(new ContentTestResult.DummyItem("ABC", "XYZ", 1));
        contentTestResult.addItem(new ContentTestResult.DummyItem("EFG", "MNO", 2));

        rvAdapter=new RVAdapter(contentTestResult.ITEMS);
        recyclerView.setAdapter(rvAdapter);

        return view;
    }

    private class RVAdapter extends RecyclerView.Adapter<RVAdapter.CardViewHolder> {
        ContentTestResult content = new ContentTestResult();

        public RVAdapter ( List<ContentTestResult.DummyItem> list_dummy ){
            content.ITEMS = list_dummy;
        }

        @Override
        public RVAdapter.CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_test_result, parent, false );
            CardViewHolder cardViewHolder = new CardViewHolder( view );
            return cardViewHolder;
        }

        @Override
        public void onBindViewHolder(RVAdapter.CardViewHolder holder, int position) {
            holder.place.setText(content.ITEMS.get(position).place);
            holder.near.setText("Near " + content.ITEMS.get(position).near);
            holder.price.setText("No. " + content.ITEMS.get(position).price);
        }

        @Override
        public int getItemCount() {
            return content.ITEMS.size();
        }

        public class CardViewHolder extends RecyclerView.ViewHolder {
            CardView cardView;
            TextView place, near, price;

            public CardViewHolder(View itemView) {
                super(itemView);
                cardView = (CardView) itemView.findViewById(R.id.cardView_card_test);
                place = (TextView) itemView.findViewById(R.id.text_card_test_place);
                near = (TextView) itemView.findViewById(R.id.text_card_test_near);
                price = (TextView) itemView.findViewById(R.id.text_card_test_price);
            }
        }
    }


}
